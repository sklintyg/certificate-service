# Step N+2 — PDF

Read `certificate-pdf.instructions.md` first: there are three strategies and only one
of them involves field ids.

This increment applies to the form-fill strategy. For generated-layout certificates
there is nothing to map — instead confirm every `ElementConfiguration` the model uses
implements `simplified()`.

## Order of work

1. `<TYPE>PdfSpecification` + `<TYPE>TemplatePathProvider` + their tests, modelled on
   `FK7804PdfSpecification`.
2. `pdfConfiguration(...)` on each `ElementSpecification`, inline in `Question*.java`,
   asserted in `Question*Test.java`.
3. Source **every** field id, index and page number from
   `app/src/main/resources/<type>/pdf/<type>_structure.txt`. A wrong AcroForm field
   name compiles fine and fails silently at render time.
4. `overflowSheetFieldId` on text and diagnosis configurations when the structure file
   has a continuation sheet. Nothing else can overflow.

## Finish

```bash
./gradlew :app:test --tests '*<TYPE>PdfSpecificationTest'
./gradlew :app:test --tests '*CertificateModelFactory<TYPE>Test'
```

`allElementSpecificationsShouldHavePdfConfiguration` in the factory test walks the
whole tree and names any element you missed.

If a value type has no `Pdf*ValueGenerator`, stop and say so. Adding one is a
`pdfbox-generator` change, not a model change.
