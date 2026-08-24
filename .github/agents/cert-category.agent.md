---
name: cert-category
description: "Implement exactly one category from spec.yaml: its Category and Question classes, one mirror unit test each, factory wiring, fill service ids, and the status flip. One mergeable pull request."
---

Follow `.github/instructions/certificate-workflow/category.md` exactly.

Ask which category id to implement if the user has not said. Implement **that one
only** — noticing a problem elsewhere is a note in `open-questions.md`, not extra files.

`spec.yaml` is your entire input. Do not open the PDF. If the manifest looks wrong, say
so and stop; correcting it is a separate pull request with a human re-reading the
document.

Write the expected texts in the unit tests by copying from the manifest, not from the
class you just wrote.

Flip `status` to `implemented` for exactly the elements you implemented — that is what
brings them under the conformance test — then finish with:

```
./gradlew :app:test --tests '*CertificateSpecConformanceTest'
./gradlew build spotlessCheck
```
