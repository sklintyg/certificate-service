---
applyTo: "**/certificatemodel/**,**/fillservice/**"
description: Anatomy and conventions of a certificate model implementation.
---

# Certificate model anatomy

A certificate model is a `CertificateModel` built by a `CertificateModelFactory`.
It lives in `app/src/main/java/se/inera/intyg/certificateservice/infrastructure/certificatemodel/<type>/`.

`fk7804` is the canonical single-version implementation. `ts8071` (v1/v2) and
`fk3226` (v1/v1.1) are the canonical multi-version ones.

## Registration — there is no registry

`InMemoryCertificateModelRepository` injects `List<CertificateModelFactory>`, so a
factory annotated `@Component` is picked up by classpath scanning alone. Do **not**
look for a registry class, enum or config to edit — there is none. Versions of the
same `CertificateType` are grouped automatically in `@PostConstruct`.

## The factory

```java
@Component
@RequiredArgsConstructor
public class CertificateModelFactoryFK7804 implements CertificateModelFactory {

  private static final SchematronPath SCHEMATRON_PATH =
      new SchematronPath("fk7804/schematron/lisjp.v2.sch");

  @Value("${certificate.model.fk7804.v2_0.active.from}")
  private LocalDateTime activeFrom;

  @Value("${sendmessagetofk.logicaladdress}")
  private String fkLogicalAddress;

  private static final String FK_7804 = "fk7804";
  private static final String VERSION = "2.0";
  private static final CertificateTypeName FK7804_TYPE_NAME = new CertificateTypeName("FK7804");
  private static final String NAME = "Läkarintyg för sjukpenning";
  private static final String DESCRIPTION = """ ... """;          // "Text innan val av intyg"
  private static final String DETAILED_DESCRIPTION = """ ... """; // "Text efter val av intyg"

  public static final CertificateModelId FK7804_V2_0 =
      CertificateModelId.builder()
          .type(new CertificateType(FK_7804))
          .version(new CertificateVersion(VERSION))
          .build();

  @Override
  public CertificateModel create() {
    return CertificateModel.builder()
        .id(FK7804_V2_0)
        .type(CodeSystemKvIntygstyp.FK7804)
        ...
        .elementSpecifications(List.of(
            categorySmittbararpenning(questionSmittbararpenning()),
            categoryDiagnos(questionDiagnos(diagnosisCodeRepository)),
            ...
            issuingUnitContactInfo()))
        .build();
  }
}
```

Notes:

- `activeFrom` is **never** hardcoded — always `@Value` on
  `certificate.model.<type>.v<major>_<minor>.active.from`.
- The public `CertificateModelId` constant (`FK7804_V2_0`) is how other models
  cross-reference this one (`ableToCreateDraftForModel`) and how the testability
  fill service identifies it. Always declare it.
- The element tree is expressed as **nested factory-method calls**:
  `category(question(subQuestion()))`. Nothing else defines the hierarchy.
- `issuingUnitContactInfo()` (from `common/elements/`) is the last entry of every
  model's `elementSpecifications`.
- `detailedDescription` is normally written as a text block and passed through
  `.replaceAll("\\R", "")`.

### `CertificateModel` builder slots

`id`, `type`, `typeName`, `name`, `description`, `detailedDescription`, `recipient`,
`activeFrom`, `availableForCitizen`, `certificateActionSpecifications`,
`messageActionSpecifications`, `elementSpecifications`, `schematronPath`, `texts`,
`summaryProvider`, `messageTypes`, `pdfSpecification`, `confirmationModalProvider`,
`certificateActionFactory`, `sickLeaveProvider`, `citizenAvailableFunctionsProvider`,
`ableToCreateDraftForModel`, `generalPrintProvider`.

## Companion classes in the same package

| Class | Required? | Purpose |
|---|---|---|
| `<TYPE>CertificateActionSpecification` | yes | static `create()` returning `List<CertificateActionSpecification>` — which actions, which `Role`s |
| `<TYPE>MessageActionSpecification` | if the type supports *ärendekommunikation* | ditto for `MessageActionType` |
| `<TYPE>CertificateSummaryProvider` | usually | the 1177 summary line |
| `<TYPE>PdfSpecification` + `<TYPE>TemplatePathProvider` | form-fill PDF only | see `certificate-pdf.instructions.md` |
| `<TYPE>SickLeaveProvider` | fk7804 / ag7804 / ag114 only | sick-leave extract |
| `<TYPE>CitizenAvailableFunctionsProvider` | optional | citizen-facing function gating |
| `<TYPE>Certificate*ContentProvider` | optional | modal text for SEND / CREATE_DRAFT_FROM_CERTIFICATE |

## Element classes — `<type>/elements/`

One class per category, question or message. Each is a utility class with a private
constructor and a single static factory method.

```java
public class CategoryDiagnos {

  public static final ElementId CATEGORY_ID = new ElementId("KAT_4");

  private CategoryDiagnos() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryDiagnos(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(CATEGORY_ID)
        .configuration(ElementConfigurationCategory.builder().name("Diagnos").build())
        .children(List.of(children))
        .build();
  }
}
```

Conventions, followed without exception:

- `CategoryXxx` → `categoryXxx(ElementSpecification... children)`;
  `QuestionXxx` → `questionXxx(...)`; `MessageXxx` → `messageXxx()`.
  The method name is the class name with a lowercase first letter.
- **Every category class declares its own constant named exactly `CATEGORY_ID`** —
  not `CATEGORY_DIAGNOS_ID`. They are disambiguated by qualified static import.
- Questions declare `public static final ElementId QUESTION_<NAME>_ID` and
  `public static final FieldId QUESTION_<NAME>_FIELD_ID`.
- **There is no central constants file.** Each element class owns its ids. Cross-element
  references are made by static-importing the other class's constant.
- Class names are `Category`/`Question` + the Swedish label in PascalCase. Shorten a
  long label but keep it recognisable.
- Everything is wired into the factory through **static imports** — a factory with 30
  static imports is normal and correct.

### Ids

- `ElementId` for a question is the **question number from the specification** as a
  string: `"6"`, `"27"`, `"33.2"`. They are not sequential and not in visual order.
- `FieldId` is normally `"<elementId>.<n>"` (`"28.1"`), but may be a code
  (`new FieldId(ARBETSSOKANDE.code())`) or a domain word (`"huvuddiagnos"`).
- Categories have no id in the specification. Use `KAT_1`, `KAT_2`, … numbered in
  the order they appear in the specification.

### Sub-questions

A `DFR` (sub-question, id like `1.3` under `1`) needs an `ElementMapping` to its
parent so XML generation nests it correctly:

```java
.mapping(new ElementMapping(QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID, null))
```

A question with a `SHOW`/`HIDE` rule almost always also needs `shouldValidate`, built
with `ElementDataPredicateFactory`, so it is not validated while hidden.

## Versioning

A new major/minor version is a **new factory class** with a `V<X>` suffix
(`CertificateModelFactoryTS8071V2`, `CertificateModelFactoryFK3226V1_1`), plus a
restructured `elements/` directory:

```
<type>/elements/
  common/   element classes byte-identical across versions — no suffix
  v1/       classes unique to v1 — suffix V1 on class name and id constants
  v2/       classes unique to v2 — suffix V2
```

Rules:

- An element is **common only if everything matches**: texts, help texts, config type,
  limits, rules, validations, codes. A single changed character makes it version-specific.
- **A `vN` class must never import from a `vM` class.** Use the `common` or `vN` id.
- A common class that needs a version-specific id takes it as a parameter, and the
  factory passes it in — see `QuestionMissbrukProvtagning` and the TS8071 V2 factory.
- **Before** any of this: lock the previous version. See
  `certificate-tests.instructions.md` § Version lock.

## Configuration that must be updated in lockstep

Adding a model version requires the `active.from` property in **three** places, or
the Spring context fails to start:

1. `devops/dev/config/application-dev.properties`
2. `integration-test/src/test/resources/config/application-integration-test.properties`
3. the `@DynamicPropertySource` blocks in **both**
   `integration-test/.../common/setup/ActiveCertificatesIT.java` and
   `InActiveCertificatesIT.java`

Also register the external type code in
`common/codesystems/CodeSystemKvIntygstyp.java`.
