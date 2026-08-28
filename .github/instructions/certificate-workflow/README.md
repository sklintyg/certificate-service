# Implementing a certificate model

Read this before starting. A certificate is implemented as a **sequence of small
merged pull requests**, never in one pass. A single category of FK7804 is a few
hundred lines of Java plus the same again in tests; a whole certificate is several
thousand. Nobody — human or model — reviews that reliably in one sitting.

## The shape of it

```
  specification PDF
        │  cert-spec-extract        (once, reviewed by a human)
        ▼
     spec.yaml  ──────────────┐
        │  cert-scaffold      │
        │  cert-category ×N   │  every step reads the manifest,
        │  cert-schematron    │  nothing re-reads the PDF
        │  cert-pdf           │
        ▼                     ▼
      code  ────────▶ CertificateSpecConformanceTest
                      (fails the build on any drift)
```

`spec.yaml` is the contract. It describes the **whole** certificate from day one,
with every element marked `pending`. Each category increment implements a slice and
flips those elements to `implemented`, which is what brings them under the
conformance test. So the manifest is both the specification and the progress ledger:

```bash
grep -c 'status:.*pending'     app/src/test/resources/certificate-specs/<type>/v<M>_<N>/spec.yaml
grep -c 'status:.*implemented' app/src/test/resources/certificate-specs/<type>/v<M>_<N>/spec.yaml
```

Matched loosely on purpose: a generated manifest quotes the value and a hand-written one
need not, and a grep for `status: "pending"` silently reports zero against the second.

## The increments

Each row is one pull request that leaves `main` green.

| # | Agent | Pull request contains |
|---|---|---|
| 0 | `cert-spec-extract` | `spec.yaml` + `open-questions.md`. **No Java.** A human reviews it against the PDF. |
| 1 | `cert-scaffold` | Factory with no categories, action/message specs, `active.from` in three places, `CodeSystemKvIntygstyp` entry, empty fill service, factory test, `TestSetup` + Active/Citizen/Inactive ITs, prefill XML |
| 2…N | `cert-category` | One category: its `Category*` and `Question*` classes, one mirror unit test each, factory wiring, fill-service ids, status flips |
| N+1 | `cert-schematron` | `.sch` resource, `SchematronPath`, `SchematronValidation<TYPE>Test` |
| N+2 | `cert-pdf` | `PdfSpecification`, `TemplatePathProvider`, templates, per-question `pdfConfiguration`, the `InactiveTypeIT` mount |
| N+3 | — | Remaining `@Nested` IT scenario mounts and role matrices |

Category increments do not depend on each other, so several people can take one each.

For a **new version of an existing certificate**, increment 0 is preceded by a
version-lock pull request (see `version-diff.md`) and the manifest is produced by
`cert-version-diff` rather than by extraction alone.

## Rules that apply to every increment

1. **Never invent a code, id, or text.** Missing values become a `TODO:` constant and
   a line in `open-questions.md`. A guess that looks plausible is worse than a gap.
2. **Never edit `spec.yaml` to make a test pass.** The manifest is reviewed against
   the specification; the code is not. If they disagree, the code is wrong. A genuine
   manifest error is its own pull request with a human re-reading the document.
3. **Finish green.** `./gradlew build spotlessCheck` must pass before you open the
   pull request. If a category will not come together, leave it out of the model
   rather than committing it half-wired.
4. **Do not widen the increment.** Noticing that a later category needs a new code
   system is a note in `open-questions.md`, not extra files in this pull request.

## Feedback loop

```bash
./gradlew :app:test --tests '*CertificateSpecConformanceTest'    # spec drift, seconds
./gradlew :app:test --tests '*<Type>*'                           # the model's unit tests
./gradlew build spotlessCheck                                    # what CI runs
./gradlew integrationtest                                        # needs Docker
```

Run the first one constantly. It is the cheapest check that the Swedish strings,
limits, codes and rules you just typed match the reviewed specification.

## The playbooks

| File | Step |
|---|---|
| `spec-extract.md` | PDF → manifest |
| `open-questions-template.md` | the shape of the questions a manifest cannot answer |
| `scaffold.md` | the model skeleton |
| `category.md` | one category |
| `schematron.md` | XML validation |
| `pdf.md` | PDF configuration |
| `verify.md` | audit an implemented certificate against its manifest |
| `version-diff.md` | a new version of an existing certificate |
