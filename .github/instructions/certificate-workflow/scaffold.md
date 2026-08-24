# Step 1 — the model skeleton

**Input:** a reviewed `spec.yaml`.
**Output:** a registered, activatable certificate model with **no categories yet**,
and the whole test scaffolding around it. Green build, mergeable.

The point of shipping an empty model first is that everything fiddly and
easy-to-forget — the three property locations, the code-system entry, the four
integration-test files — is done and verified once, in a pull request small enough
to read, before any question exists to argue about.

## Files

Under `app/src/main/java/.../infrastructure/certificatemodel/<type>/`:

- `CertificateModelFactory<TYPE>.java` — `@Component`, `@Value` on
  `certificate.model.<type>.v<M>_<N>.active.from`, the public `CertificateModelId`
  constant, `create()` with metadata from `spec.yaml` and
  `elementSpecifications(List.of(issuingUnitContactInfo()))`.
- `<TYPE>CertificateActionSpecification.java` — model it on the closest existing
  certificate with the same recipient. The **Funktioner** notes in
  `open-questions.md` decide which actions are present.
- `<TYPE>MessageActionSpecification.java` — only if the type supports
  *ärendekommunikation*.
- `<TYPE>CertificateSummaryProvider.java` if the specification describes a 1177 summary.

Elsewhere:

- `common/codesystems/CodeSystemKvIntygstyp.java` — the external code from
  `certificate.externalCode`.
- `testability/certificate/service/fillservice/<type>/TestabilityCertificateFillService<TYPE>.java`
  with empty `MINIMAL_IDS` / `MAXIMAL_IDS`.
- `app/src/test/.../certificatemodel/<type>/CertificateModelFactory<TYPE>Test.java`.
- `integration-test/.../integrationtest/<type>/` — `<TYPE>TestSetup`, `<TYPE>ActiveIT`,
  `<TYPE>CitizenIT`, `<TYPE>InactiveIT`.
- `integration-test/src/test/resources/prefill/<TYPE>_V<major>.xml`.
- Register the model in `CertificateSpecConformanceTest.MODELS`.

## The three property locations

Miss one and the Spring context will not start:

1. `devops/dev/config/application-dev.properties`
2. `integration-test/src/test/resources/config/application-integration-test.properties`
3. the `@DynamicPropertySource` blocks in **both**
   `integration-test/.../common/setup/ActiveCertificatesIT.java` and
   `InActiveCertificatesIT.java`

## Integration tests

Start with the scenarios that do not need a single question answered:
`CreateCertificateIT`, `DeleteCertificateIT`, `GetCertificateIT`, `InactiveTypeIT`,
and the citizen list scenarios. Everything that writes an answer waits until a
category exists — `<TYPE>TestSetup.valueForTest` needs a real element, so add the
remaining `@Nested` mounts in the increment that gives it one.

## Finish

```bash
./gradlew build spotlessCheck
./gradlew integrationtest --tests '*<TYPE>*'
```

The conformance test will still skip: nothing is `implemented` yet. That is correct.
