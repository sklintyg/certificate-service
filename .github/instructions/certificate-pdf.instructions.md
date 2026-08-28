---
applyTo: "**/certificatemodel/**,**/pdfboxgenerator/**"
description: PDF generation for a certificate model — the three strategies and how to wire each.
---

# PDF generation

**There is not one PDF path — there are three.** Pick before writing anything;
`PdfGeneratorProvider` dispatches on what the model declares.

| Strategy | Model declares | Used by | Needs |
|---|---|---|---|
| Form-fill (AcroForm template) | `CustomPdfSpecification` | all `fk*` types | template PDFs, structure file, `TemplatePathProvider`, per-question `pdfConfiguration` |
| Generated layout | `GeneralPdfSpecification` | `ts8071` | only a `description`; rendering comes from `ElementConfiguration.simplified()` |
| General print provider | no `pdfSpecification`, a `CertificateGenerelPrintProvider` | `ag114` | a print provider + `CertificateGeneralPrintText` |

If the certificate has no supplied AcroForm template, it is **not** form-fill. Do not
invent field ids.

## Form-fill wiring

Per type:

- `<type>/<TYPE>PdfSpecification.java` — `CustomPdfSpecification.builder()` with
  `pdfTemplatePathProvider`, `patientIdFieldIds`, a `CustomPdfSignature` (signed date,
  signed-by name, PA title, specialty, HSA id, workplace code, contact information),
  an `OverlayTextProvider(OverlayDetails…)`, and `overflowFieldId` / `overFlowPageIndex`.
- `<type>/<TYPE>TemplatePathProvider.java` — `extends AbstractTemplatePathProvider`,
  overriding `pathWithAddress()` and `pathWithoutAddress()`.
- Resources: `app/src/main/resources/<type>/pdf/<type>_v<N>.pdf`,
  `<type>_v<N>_no_address.pdf`, and `<type>_structure.txt`.
- Tests: `<TYPE>PdfSpecificationTest`, `<TYPE>TemplatePathProviderTest`.

**`<type>_structure.txt` is the source of truth** for every field id, index and page
number. Read values out of it. Never guess an AcroForm field name — they look like
`form1[0].#subform[0].flt_txtDiaKod1[0]` and a wrong one fails silently at render
time, not at compile time.

## Per-question configuration

`PdfConfiguration` is set **inline on the `ElementSpecification`** inside
`Question*.java` — not in a separate file — and asserted in `Question*Test.java`.

| `ElementValue` | `PdfConfiguration` | Generator |
|---|---|---|
| `ElementValueText` | `PdfConfigurationText` | `PdfTextValueGenerator` |
| `ElementValueBoolean` | `PdfConfigurationBoolean` / `PdfConfigurationRadioBoolean` | `PdfBooleanValueGenerator` |
| `ElementValueCode` | `PdfConfigurationCode` / `PdfConfigurationRadioCode` / `PdfConfigurationDropdownCode` | `PdfCodeValueGenerator` |
| `ElementValueCodeList` | `PdfConfigurationCode` | `PdfCodeListValueGenerator` |
| `ElementValueDate` | `PdfConfigurationDate` / `PdfConfigurationDateCheckbox` | `PdfDateValueGenerator` |
| `ElementValueDateList` | `PdfConfigurationDateList` | `PdfDateListValueGenerator` |
| `ElementValueDateRange` | `PdfConfigurationDateRange` / `PdfConfigurationDateRangeCheckbox` | `PdfDateRangeValueGenerator` |
| `ElementValueDateRangeList` | `PdfConfigurationDateRangeList` | `PdfDateRangeListValueGenerator` |
| `ElementValueDiagnosisList` | `PdfConfigurationDiagnoses` (+ `PdfConfigurationDiagnosis` per row) | `PdfDiagnosisListValueGenerator` |
| `ElementValueMedicalInvestigationList` | `PdfConfigurationMedicalInvestigationList` | `PdfMedicalInvestigationListValueGenerator` |
| — (not rendered) | `PdfConfigurationHidden` | — |

Overflow: only text and diagnoses can overflow. When the structure file has a
continuation sheet, set `overflowSheetFieldId` on those configurations.

If a question's value type has no generator in this table, **stop and say so** rather
than inventing a mapping — a new generator is a `pdfbox-generator` change, not a
model change.

## Order of work

1. `<TYPE>PdfSpecification` + its test, modelled on `FK7804PdfSpecification`.
2. `pdfConfiguration` per question, sourcing every id and index from the structure file.
3. Re-run `CertificateModelFactory<TYPE>Test.allElementSpecificationsShouldHavePdfConfiguration`,
   which catches any question you missed.

For the generated-layout and print-provider strategies, there is no field mapping at
all — instead make sure every `ElementConfiguration` the model uses implements
`simplified()`.
