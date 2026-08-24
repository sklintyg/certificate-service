# Step N+1 — schematron

**Applies to certificates sent to Försäkringskassan.** AG and TS types have no
schematron; skip this increment for them.

The XML layer itself is generic — one `XmlGenerator*` per `ElementValue`, driven by
`ElementSpecification.mapping` and `includeInXml`. There is no per-certificate
converter to write.

## Files

- `app/src/main/resources/<type>/schematron/<name>.v<N>.sch` — supplied by the
  integration, not written here.
- `SchematronPath` constant in the factory, wired with `.schematronPath(...)`.
- `app/src/test/.../certificatemodel/<type>/schematron/SchematronValidation<TYPE>Test.java`
  — wires a real `XmlGeneratorCertificateV4`, generates XML from `TestDataCertificate`
  plus `ElementData`, and asserts the schematron accepts a complete certificate and
  rejects an incomplete one.
- `integration-test/src/test/resources/prefill/<TYPE>_V<major>.xml` — fill it in
  properly now; `CreateCertificateIT.shallReturnCertificateWithPrefilledAnswers`
  asserts a prefilled draft validates with zero errors, which is a strong end-to-end
  check of the mappings.

## Checks

Every `DFR` element needs an `ElementMapping` to its parent, or it serialises at the
wrong level and the schematron rejects it. Elements that must not appear in the XML
get `.includeInXml(false)`.

A bespoke XML shape needs a new `CustomMapperId` and an `XmlGeneratorCustomMapper`.
That is a domain change — raise it rather than doing it inside this increment.

```bash
./gradlew :app:test --tests '*SchematronValidation<TYPE>Test'
./gradlew integrationtest --tests '*<TYPE>*'
```
