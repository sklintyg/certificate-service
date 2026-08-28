# Certificate specification manifests

One `spec.yaml` per certificate version, at `<type>/v<major>_<minor>/spec.yaml`.

Each is the machine-readable form of that certificate's *intygsspecifikation*: authored
from the specification document, reviewed against it by a human, and then enforced by
`CertificateSpecConformanceTest`, which fails the build if the implemented model drifts
from it in any text, limit, code or rule.

- Schema and field meanings: `.github/certificate-spec.schema.json`
- How one is produced: `.github/instructions/certificate-workflow/spec-extract.md`
- How it drives the implementation: `.github/instructions/certificate-workflow/README.md`

**Do not edit a manifest to make a test pass.** The manifest is reviewed against the
specification; the code is not. If they disagree, the code is wrong.
