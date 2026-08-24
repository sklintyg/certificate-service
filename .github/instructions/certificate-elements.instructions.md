---
applyTo: "**/certificatemodel/**"
description: Specification codes (SK/SR/SM) mapped to Java configuration, value, validation and rule types.
---

# Specification codes → Java types

The specification tables use three families of codes. This file maps all of them.
The mapping tables below were derived from the source, not from memory; if you find
a case they do not cover, add it here rather than improvising.

## SK-xxx — *Inputkomponent* → `ElementConfiguration`

| SK | Datatype in spec | `ElementConfiguration` | `ElementValue` | `ElementValidation` | Reference implementation |
|---|---|---|---|---|---|
| SK-000 | — | `ElementConfigurationCategory` | none | none (`…Category` if the category itself is mandatory) | `CategoryDiagnos` |
| SK-001 | boolesk | `ElementConfigurationCheckboxBoolean` | `ElementValueBoolean` | `ElementValidationBoolean` | `QuestionSmittbararpenning` |
| SK-002 | boolesk | `ElementConfigurationRadioBoolean` | `ElementValueBoolean` | `ElementValidationBoolean` | `QuestionTransportstod` |
| SK-002 | kodverk | `ElementConfigurationRadioMultipleCode` | `ElementValueCode` | `ElementValidationCode` | `QuestionPrognos` |
| SK-004 | kodverk | `ElementConfigurationCheckboxMultipleCode` | `ElementValueCodeList` | `ElementValidationCodeList` | `QuestionSysselsattning` |
| SK-004a | koder med datum | `ElementConfigurationCheckboxMultipleDate` | `ElementValueDateList` | `ElementValidationDateList` | `QuestionUtlatandeBaseratPa` |
| SK-004a | koder med datumperioder | `ElementConfigurationCheckboxDateRangeList` | `ElementValueDateRangeList` | `ElementValidationDateRangeList` | `QuestionNedsattningArbetsformaga` |
| SK-005 | datum | `ElementConfigurationDate` | `ElementValueDate` | `ElementValidationDate` | `QuestionNarAktivaBehandlingenAvslutades` |
| SK-005 | datumperiod | `ElementConfigurationDateRange` | `ElementValueDateRange` | `ElementValidationDateRange` | `QuestionPeriodVardEllerTillsyn` |
| SK-006 | text (kort) | `ElementConfigurationTextField` | `ElementValueText` | `ElementValidationText` | `QuestionYrkeOchArbetsuppgifter` |
| SK-007 | text (lång) | `ElementConfigurationTextArea` | `ElementValueText` | `ElementValidationText` | `QuestionOvrigt` |
| SK-007 | text med ICF-stöd | `ElementConfigurationIcf` | `ElementValueIcf` | `ElementValidationIcfValue` | `QuestionAktivitetsbegransningar` |
| SK-008 | kodverk (lista) | `ElementConfigurationDropdownCode` | `ElementValueCode` | `ElementValidationCode` | `QuestionKannedomOmPatienten` |
| SK-009 | numeriskt | `ElementConfigurationInteger` | `ElementValueInteger` | `ElementValidationInteger` | `QuestionAntalManader` |
| SK-A01…A04 | information | `ElementConfigurationMessage` | none | none | `MessageNedsattningArbetsformagaStartDateInfo` |
| — | diagnoser | `ElementConfigurationDiagnosis` | `ElementValueDiagnosisList` | `ElementValidationDiagnosis` | `QuestionDiagnos` |
| — | utredning/underlag | `ElementConfigurationMedicalInvestigationList` | `ElementValueMedicalInvestigationList` | `ElementValidationMedicalInvestigationList` | `QuestionUtredningEllerUnderlag` |
| — | synskärpa | `ElementConfigurationVisualAcuities` | `ElementValueVisualAcuities` | `ElementValidationVisualAcuities` | `QuestionSynskarpa` |

Two more exist and are not selected from a specification row:

- `ElementConfigurationCode` — not a question. It is one *option* inside a
  checkbox/radio list, built with `CodeFactory.elementConfigurationCode(CODE)`.
- `ElementConfigurationUnitContactInformation` — the shared trailing element,
  `issuingUnitContactInfo()`. Every model ends with it.

`SK-006` vs `SK-007` matters: a text limit change or a `TextField` → `TextArea`
change is a **different question**, and across versions it forces a new `VN` class.

## SR-xxx — *Regel* → `ElementRule`

Build every rule through `CertificateElementRuleFactory` (note the name — it is
**not** `ElementRuleFactory`). Never construct `ElementRuleExpression` by hand if a
factory method fits.

| SR | Meaning | Factory method | `ElementRuleType` |
|---|---|---|---|
| SR-001 | Mandatory question | `mandatory` / `mandatoryExist` / `mandatoryOrExist` / `mandatoryAndExist` / `mandatoryNotEmpty` | `MANDATORY` |
| SR-002 | Mandatory category | (category rule) | `CATEGORY_MANDATORY` |
| SR-003 | Show question | `show` / `showEmpty` / `showIfNot` / `showOrExist` | `SHOW` |
| SR-004 | Hide question | `hide` | `HIDE` |
| SR-005 / SR-008 | Disable component | `disableElement` / `disableEmptyElement` / `disableSubElements` | `DISABLE`, `DISABLE_SUB_ELEMENT` |
| SR-006 | Prefill / autofill | `autofill` | `AUTO_FILL` |
| *"Antal tecken: N"* | Text limit | `limit(id, (short) N)` | `TEXT_LIMIT` |

### `mandatory` vs `mandatoryExist`

`mandatory(id, fieldId)` produces `$fieldId`, which is only true for a **checked
checkbox or a `true` boolean**. For anything where an answer of `false` or an empty
selection is still a valid answer — `RadioBoolean`, code lists, dates — you need
`exists(...)`:

- `RadioBoolean` (Ja/Nej) → `mandatoryExist`, because `Nej` is an answer.
- Several fields where any one satisfies the requirement → `mandatoryOrExist`.
- Several fields where all are required → `mandatoryAndExist`.
- Free text that must be non-blank → `mandatoryNotEmpty`.

Getting this wrong produces a certificate that cannot be signed after answering
"Nej". It is the single most common defect in this area.

### Show/hide implies `shouldValidate`

A question that can be hidden must not be validated while hidden. Add a
`shouldValidate` predicate built with `ElementDataPredicateFactory`:

```java
.shouldValidate(ElementDataPredicateFactory.checkboxBoolean(QUESTION_SMITTBARARPENNING_ID, false))
```

Available predicates: `valueBoolean`, `radioBooleans`, `checkboxBoolean`, `codes`,
`codeList`, `dateRangeList`, `visualAcuities`.

### Composite expressions

For conditions the named helpers cannot express, compose a `RuleExpression` from
`CertificateElementRuleFactory`'s string helpers rather than writing the expression
literal by hand: `singleExpression`, `multipleOrExpression`, `multipleAndExpression`,
`multipleOrExpressionWithExists`, `multipleOrExpressionWithNotEmpty`, `not`, `exists`,
`notEmpty`, `empty`, `lessThan`, `lessThanOrEqual`, `equals`, `today`, `withCitation`,
`wrapWithParenthesis`, `wrapWithAttribute`, `wrapWithNotEmpty`.

## SM-xxx — *XML-mappning* → `ElementMapping`

The XML layer is generic: one `XmlGenerator*` per `ElementValue`, driven by
`ElementSpecification.mapping` and `includeInXml`. You do **not** write a converter
per certificate.

- A top-level question (`FRG`) usually needs no mapping.
- A sub-question (`DFR`, id `1.3` under `1`) needs
  `.mapping(new ElementMapping(PARENT_ID, null))`.
- A question that must not be serialised gets `.includeInXml(false)`.
- Only bespoke shapes need a `CustomMapperId`; the enum currently holds just
  `CODE_LIST_TO_BOOLEAN` and `UNIFIED_DIAGNOSIS_LIST`. Adding one is a domain change
  and needs an `XmlGeneratorCustomMapper` implementation — flag it, do not do it
  silently.

## Code systems

Coded options come from `common/codesystems/CodeSystemKv*`. The spec gives the
*Kodverk* (`KV_FKMU_0002`) and the *Urval* (`[NUVARANDE_ARBETE, ARBETSSOKANDE, …]`).

- If the code system class exists, static-import the constants and use
  `CodeFactory.elementConfigurationCode(NUVARANDE_ARBETE)`.
- If it does not exist, create `CodeSystemKv<Name>` following the existing shape
  (`CODE_SYSTEM` constant, one `public static final Code` per option, private
  constructor).
- **Never invent a code value or display name.** If the spec does not spell it out,
  emit the constant with a `TODO:` and record it in `open-questions.md`.

## Text fields in the specification

| Specification wording | Model field |
|---|---|
| "Text innan val av intyg" | `CertificateModel.description` |
| "Text efter val av intyg" | `CertificateModel.detailedDescription` |
| "Rubriktext" | `ElementConfiguration.name` |
| "Hjälptext" (Hjälptexter section) | `ElementConfiguration.description` |
| "Label för checkbox" / "Label för fritextfält" | `ElementConfiguration.label` |
| "Rubrik:" prefix inside a Rubriktext cell | `ElementConfiguration.header` |
| "Text i popover/tooltip" | `ElementConfiguration.description` on the sub-element |

Copy them **word for word**, including punctuation and casing. `CertificateSpecConformanceTest`
compares these byte for byte against the spec manifest and will fail the build on any
difference.
