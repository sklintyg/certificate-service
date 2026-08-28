# Audit — code against manifest

Run this before declaring a certificate finished, and whenever a review needs an
independent read.

`CertificateSpecConformanceTest` compares the manifest against the model. This step
goes the **other way**: read the source, describe what it actually does in the
manifest's vocabulary, and diff that against `spec.yaml`. An error that the
implementation and the manifest happen to share survives the test but not this.

## How

1. Produce the code's own view:
   ```bash
   ./gradlew :app:test --tests '*SpecManifestBootstrapTest' -DbootstrapManifest=<type>/v<M>_<N>
   ```
   This overwrites `spec.yaml`, so do it on a scratch copy — `git stash` or a copy of
   the file — never on the reviewed manifest.
2. Diff the generated file against the reviewed one.
3. For each difference decide which side is wrong, and say which. Do not fix both to
   match.

## Then check what the manifest cannot express

The conformance test deliberately does not encode rule *expressions*, only rule
*types*. Read these by hand:

- Every `SHOW` / `HIDE` expression points at the element and value the specification's
  rule cell names (`FrågeId: 28.1, Värde: NUVARANDE_ARBETE`).
- Every hideable element has a `shouldValidate` predicate, so it is not validated
  while hidden.
- `mandatory` versus `mandatoryExist` — a `RadioBoolean` answered "Nej" must satisfy
  its mandatory rule.
- Elements still marked `pending` are genuinely absent from the model, not silently
  half-implemented.
- Every element in the model appears in the manifest. An element the specification
  never mentioned is as much a defect as a missing one.

## Output

A short report: differences found, which side is wrong for each, and anything in
`open-questions.md` still unanswered. Do not change code in this step.
