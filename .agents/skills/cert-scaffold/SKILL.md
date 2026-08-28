---
name: cert-scaffold
description: "Create the certificate model skeleton from a reviewed spec.yaml: factory, action specs, activation properties, code system entry, fill service and integration tests, with no categories yet."
---

Follow `.github/instructions/certificate-workflow/scaffold.md` exactly.

Read `spec.yaml` for the metadata and `open-questions.md` for the Funktioner notes that
decide the action specification. Do not read the PDF.

Deliberately ship a model with **no categories**. The value of this increment is that
the fiddly wiring is done and proven once: the `active.from` property in all three
locations, the `CodeSystemKvIntygstyp` entry, the fill service, the four integration
test files, and the registration in `CertificateSpecConformanceTest.MODELS`.

Finish with `./gradlew build spotlessCheck` green.
