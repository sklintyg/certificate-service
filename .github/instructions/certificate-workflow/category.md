# Step 2…N — one category

**Input:** `spec.yaml` and the id of one category, e.g. `KAT_3`.
**Output:** that category and its questions, implemented, tested, wired in, and
flipped to `implemented` in the manifest. Green build, mergeable.

**Do exactly one category.** If you notice something wrong in another, write it in
`open-questions.md` and leave it.

## Order of work

1. **Read the category out of `spec.yaml`.** That is your whole input. Do not open
   the PDF — if the manifest is wrong, that is a separate pull request.

2. **Write the `Category*` class.** `CATEGORY_ID` from the manifest `id`, `name` and
   `description` copied verbatim.

3. **Write one `Question*` class per element**, parents before children:
   - `component` selects the `ElementConfiguration` — see the table in
     `certificate-elements.instructions.md`.
   - `label` → `name`, `helpText` → `description`, `checkboxLabel` → `label`,
     `header` → `header`. Copy character for character.
   - `fieldId` → the configuration's `FieldId`.
   - `maxLength` → `CertificateElementRuleFactory.limit(id, (short) n)` **and** the
     matching `ElementValidation*.limit`.
   - `rules` → the factory method for each `SR` code. Check `mandatory` versus
     `mandatoryExist` — for a `RadioBoolean`, "Nej" is an answer, so it needs `exist`.
   - `options` → `CodeFactory.elementConfigurationCode(...)` per code, in manifest
     order. Create `CodeSystemKv*` if it does not exist; never invent a code value.
   - A `SHOW` or `HIDE` rule almost always also needs `shouldValidate` via
     `ElementDataPredicateFactory`.
   - `xmlParent`, when the manifest sets it → `.mapping(new ElementMapping(parentId, null))`.

     ⚠ **Unresolved, and it needs a decision.** `schematron.md` says every `DFR`
     element needs an `ElementMapping` to its parent or it serialises at the wrong
     level. This playbook only adds one when the manifest sets `xmlParent`, and
     `spec-extract.md` records `xmlParent` only when the XML structure differs from the
     question structure. For a `DFR` whose specification gives just a `TextId`, the two
     rules disagree. The conformance test cannot adjudicate — it checks `xmlParent`
     only when the manifest sets it.

     Until someone who knows the XML rules on it, follow the manifest here and raise
     the element in `open-questions.md`. If `schematron.md` is right, the fix belongs
     in `spec-extract.md` — `xmlParent` would then be recorded for every nesting `DFR`,
     not just the exceptional ones.

4. **Write one mirror unit test per class.** Copy the expected texts from the
   manifest, not from the class you just wrote — a test that copies the
   implementation only proves the code equals itself.

5. **Wire the category into the factory**, nested to match the manifest's `parent`
   relationships, and extend the `@ValueSource` id list in the factory test.

6. **Add the new ids to the fill service** — `MAXIMAL_IDS` always, `MINIMAL_IDS` if
   the element is mandatory, plus a `TEXT_QUESTION_MOCKS` entry for each text field.

   **Watch which code the minimal fill picks.** A code that reveals a mandatory
   follow-up produces a draft that cannot be signed, and the failure surfaces far from
   the cause — as an empty citizen list, or a signing scenario that returns nothing.
   Reserve such codes for `MAXIMAL_IDS`, where the follow-up is filled too.

7. **Flip `status` to `implemented`** for exactly the elements you implemented. This
   is the step that puts them under the conformance test.

8. **If this category adds a mandatory element**, fill in
   `integration-test/src/test/resources/prefill/<TYPE>.xml` for it now.
   `schematron.md` is where the fixture is finished, but
   `CreateCertificateIT.shallReturnCertificateWithPrefilledAnswers` asserts a prefilled
   draft validates with zero errors — so the first mandatory element is what breaks it.
   An empty fixture passes until then, asserting nothing.

9. **If this is the first category**, point `<TYPE>TestSetup.valueForTest` at one of
   its writable elements and mount the remaining `@Nested` IT scenarios.

## Finish

```bash
./gradlew :app:test --tests '*CertificateSpecConformanceTest'
./gradlew :app:test --tests '*<Type>*'
./gradlew build spotlessCheck
```

If the conformance test reports a difference, the code is wrong. Do not touch
`spec.yaml`.
