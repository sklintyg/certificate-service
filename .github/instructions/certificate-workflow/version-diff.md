# A new version of an existing certificate

**Input:** the new version's specification and the existing implementation.
**Output:** a version-lock pull request, then a manifest, then the usual increments.

Compare **by id**, never by class or file name. Names drift; ids are the contract.

## Increment −1: lock the current version first

Nothing else may happen before this lands.

1. Add the current version to `lockedVersions()` in
   `app/src/test/.../certificatemodel/VersionLockTest.java`, constructing the factory
   in the `static` block with a **fixed** `activeFrom`.
2. Run it — it writes
   `app/src/test/resources/certificate-model-snapshots/<type>-v<M>.<N>.json` and fails
   with instructions.
3. Read the snapshot and check it looks right.
4. Run again; it passes. Commit both.

Also bootstrap a manifest for the current version if it does not have one (see
`verify.md`), so the old version is protected by the conformance test too. The
refactor in the next step moves shared element classes around, and these two tests are
what prove it moved nothing else.

## Increment 0: the new manifest

Extract the new specification per `spec-extract.md`, then, for every element, compare
against the existing implementation and record the decision:

- **common** — every text, help text, component, limit, rule, validation and code is
  identical. Only then.
- **new in this version** — anything else, including a single changed character, a
  `TextField` → `TextArea` change, or a changed limit.
- **removed** — present in the old version, absent from the new specification.

A difference in file name or method name is not a version difference. A difference in
one character of Swedish text is.

Put the decisions in `version-analysis.md` beside the manifest: a table of
id → decision → reason. That table is what a human reviews, and it is what the
restructuring increment executes.

## Increment 1: restructure

```
<type>/elements/
  common/   identical across versions — no suffix
  v1/       unique to v1 — suffix V1 on class names and id constants
  v2/       unique to v2 — suffix V2
```

- **A `vN` class must never import from a `vM` class.** Same-named ids across versions
  are a trap; always use the `common` or `vN` constant.
- A common class needing a version-specific id takes it as a parameter, passed in by
  each factory — see `QuestionMissbrukProvtagning` and `CertificateModelFactoryTS8071V2`.
- `VersionLockTest` and the old version's conformance test must both still pass. If
  either fails, the refactor changed the old version — that is the bug this increment
  exists to prevent.

Then continue with `scaffold.md` and `category.md` for the new version, which only
needs to implement the elements marked *new in this version*.
