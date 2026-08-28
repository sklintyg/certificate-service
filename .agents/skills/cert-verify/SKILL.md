---
name: cert-verify
description: "Audit an implemented certificate by reading the code and diffing it against its spec.yaml, catching errors the conformance test cannot."
---

Follow `.github/instructions/certificate-workflow/verify.md` exactly.

This runs the comparison in the opposite direction from `CertificateSpecConformanceTest`:
it derives the manifest from the source, so an error the implementation and the manifest
happen to share still shows up.

Work on a scratch copy — `SpecManifestBootstrapTest` overwrites `spec.yaml`, and the
reviewed manifest must not be clobbered.

Change no code. Produce a report: differences found, which side is wrong for each, and
any unanswered entries in `open-questions.md`.
