---
applyTo: "**/*Test.java,**/*IT.java,**/fillservice/**"
description: Test patterns for certificate models — unit, testability, integration, version lock.
---

# Testing a certificate model

Five layers. A category increment touches the first two; the rest are their own
increments.

| Layer | Location | Granularity |
|---|---|---|
| Element unit tests | `app/src/test/.../certificatemodel/<type>/elements/` | one `*Test` per element class |
| Model factory test | `app/src/test/.../certificatemodel/<type>/CertificateModelFactory<TYPE>Test.java` | one |
| Spec conformance | `app/src/test/.../certificatemodel/spec/CertificateSpecConformanceTest.java` | automatic, driven by `spec.yaml` |
| Testability fill service | `app/src/main/.../testability/certificate/service/fillservice/<type>/` | one `@Component` |
| Integration tests | `integration-test/src/test/.../integrationtest/<type>/` | four files |

## Element unit tests

Mirror the production package exactly. No base class, no shared fixture — plain
package-private JUnit 5, one test method per `ElementSpecification` accessor, each
re-creating the element locally.

```java
class QuestionSysselsattningTest {

  @Test
  void shouldHaveCorrectId() {
    final var element = QuestionSysselsattning.questionSysselsattning();
    assertEquals(QUESTION_SYSSELSATTNING_ID, element.id());
  }

  @Test
  void shouldHaveCorrectConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCheckboxMultipleCode.builder()
            .id(QUESTION_SYSSELSATTNING_FIELD_ID)
            .name("I relation till vilken sysselsättning bedömer du arbetsförmågan?")
            .elementLayout(ElementLayout.ROWS)
            .list(List.of(...))
            .description("Om du kryssar i flera val ...")
            .build();

    final var element = QuestionSysselsattning.questionSysselsattning();
    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() { /* assertEquals(expectedRules, element.rules()); */ }

  @Test
  void shouldIncludeValidation() { /* assertEquals(expectedValidations, element.validations()); */ }

  @Test
  void shouldHaveCorrectPdfConfiguration() { /* assertEquals(expected, element.pdfConfiguration()); */ }

  @Nested
  class ShouldValidate {
    @Test
    void shallReturnFalseIfElementTrue() {
      final var elementData = List.of(ElementData.builder()
          .id(new ElementId("27"))
          .value(ElementValueBoolean.builder().value(true).build())
          .build());
      final var shouldValidate = QuestionSysselsattning.questionSysselsattning()
          .elementSpecification(QUESTION_SYSSELSATTNING_ID).shouldValidate();
      assertFalse(shouldValidate.test(elementData));
    }
  }
}
```

Points that matter:

- The expected object is **built by hand**, never derived from the production
  object. Whole-object `assertEquals` works because everything is Lombok `@Value` /
  records.
- Write the expected texts by copying from the spec manifest, not from the
  production class you just wrote — otherwise the test only proves the code equals
  itself.
- `@Nested class ShouldValidate` whenever the element has a `shouldValidate`
  predicate; cover true, false and missing.
- Both `shall*` and `should*` prefixes exist in the codebase. Match the neighbouring
  files in the package you are editing.

## Model factory test

Construct the factory directly and inject `@Value` fields:

```java
certificateModelFactory = new CertificateModelFactoryFK7804(certificateActionFactory, diagnosisCodeRepository);
ReflectionTestUtils.setField(certificateModelFactory, "activeFrom", LocalDateTime.now(ZoneId.systemDefault()));
ReflectionTestUtils.setField(certificateModelFactory, "fkLogicalAddress", "L-A");
```

Then assert metadata, and keep the two structural sweeps:

- `allElementSpecificationsShouldHavePdfConfiguration()` — recursive check (form-fill types only).
- `@ParameterizedTest @ValueSource(strings = {...})` over **every** `ElementId` in the
  model, asserting `elementSpecificationExists`. Extend this list in every category
  increment.

## Spec conformance

`CertificateSpecConformanceTest` builds the model and compares it against
`app/src/test/resources/certificate-specs/<type>/v<M>_<N>/spec.yaml` — texts byte
for byte, configuration type, field ids, limits, rules, codes, parent nesting.

It only checks elements marked `status: implemented`. A category increment flips
those entries as its last step. Run it as the fast feedback loop:

```bash
./gradlew :app:test --tests '*CertificateSpecConformanceTest'
```

Do **not** edit `spec.yaml` to make this test pass. The manifest is the reviewed
contract; if it disagrees with the code, the code is wrong. If the manifest is
genuinely wrong, that is a separate PR with a human reviewing against the PDF.

## Testability fill service

One `@Component` per model version under
`app/src/main/.../testability/certificate/service/fillservice/<type>/`. Picked up by
`TestabilityCertificateService` through `List<TestabilityCertificateFillService>`
injection — nothing to register.

```java
@Component
public class TestabilityCertificateFillServiceFK7804 implements TestabilityCertificateFillService {

  private static final List<ElementId> MAXIMAL_IDS = List.of(...);
  private static final List<ElementId> MINIMAL_IDS = List.of(...);
  private static final Map<ElementId, String> TEXT_QUESTION_MOCKS = Map.ofEntries(...);

  @Override
  public List<CertificateModelId> certificateModelIds() { return List.of(FK7804_V2_0); }

  @Override
  public List<ElementData> fill(CertificateModel model, TestabilityFillTypeDTO fillType) { ... }
}
```

Start from `configuration().emptyValue()` and pattern-match on the value type — never
hardcode configuration structure. `MINIMAL_IDS` is the set needed to sign;
`MAXIMAL_IDS` is everything answerable. Each category increment appends its ids.

## Integration tests

Four files per type in `integration-test/src/test/.../integrationtest/<type>/`:

- `<TYPE>TestSetup.java` — a `BaseTestabilityUtilities` builder: type, code, active
  version, recipient, one `valueForTest` element the generic scenarios can write to,
  and the `TestabilityAccess` flags.
- `<TYPE>ActiveIT.java` — `extends ActiveCertificatesIT`, mounts the shared scenario
  classes from `common/tests/` as `@Nested` inner classes, each overriding
  `testabilityUtilities()`.
- `<TYPE>CitizenIT.java` — the citizen-facing subset.
- `<TYPE>InactiveIT.java` — `extends InActiveCertificatesIT`, mounts `InactiveTypeIT`.

You are composing existing scenarios, not writing new test bodies. Also needed:

- a prefill fixture at `integration-test/src/test/resources/prefill/<TYPE>_V<major>.xml`
  (`CreateCertificateIT` derives the filename from the type and version);
- `active.from` entries in the `@DynamicPropertySource` blocks of **both**
  `ActiveCertificatesIT` and `InActiveCertificatesIT`.

Run with `./gradlew integrationtest`. Requires Docker (MySQL, ActiveMQ and MockServer
containers).

## Version lock

Before adding a new version of an existing certificate, lock the current one:

1. Add the previous version to `lockedVersions()` in
   `app/src/test/.../certificatemodel/VersionLockTest.java`, constructing the factory
   in the `static` block with `ReflectionTestUtils.setField(..., "activeFrom", ...)`
   set to a **fixed** date.
2. Run the test — it writes
   `app/src/test/resources/certificate-model-snapshots/<type>-v<M>.<N>.json` and fails
   with instructions.
3. Read the generated JSON and check it looks right.
4. Run again; it passes.

This must land **before** any refactor that moves elements into `common/`, otherwise
the refactor is unverifiable. If the test later fails, the diff names exactly what
changed — treat that as a regression unless the change is deliberate and documented
in the commit message.
