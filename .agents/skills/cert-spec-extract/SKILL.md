---
name: cert-spec-extract
description: "Turn a certificate specification PDF into the reviewed spec.yaml manifest that every later step reads. Run this first, on its own pull request."
---

Follow `.github/instructions/certificate-workflow/spec-extract.md` exactly.

You are producing `app/src/test/resources/certificate-specs/<type>/v<major>_<minor>/spec.yaml`
and `open-questions.md` beside it. **Write no Java in this step.**

Ask the user for the specification document before starting. If you are running in a
terminal and cannot read a PDF, say so and ask them to convert it first with
`pdftotext -layout` — without `-layout` the specification tables become unreadable.

Two things decide whether this succeeds:

- Texts are copied character for character. These strings are shown to doctors.
- Anything the document does not spell out goes in `open-questions.md`. Never guess a
  code, an id or a text.

Finish by running `./gradlew :app:test --tests '*CertificateSpecConformanceTest'` — the
well-formedness check will reject an unknown component or rule code — and then tell the
user this pull request needs a human to read `spec.yaml` against the PDF.
