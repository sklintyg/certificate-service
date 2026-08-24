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

4. **Write one mirror unit test per class.** Copy the expected texts from the
   manifest, not from the class you just wrote — a test that copies the
   implementation only proves the code equals itself.

5. **Wire the category into the factory**, nested to match the manifest's `parent`
   relationships, and extend the `@ValueSource` id list in the factory test.

6. **Add the new ids to the fill service** — `MAXIMAL_IDS` always, `MINIMAL_IDS` if
   the element is mandatory, plus a `TEXT_QUESTION_MOCKS` entry for each text field.

7. **Flip `status` to `implemented`** for exactly the elements you implemented. This
   is the step that puts them under the conformance test.

8. **If this is the first category**, point `<TYPE>TestSetup.valueForTest` at one of
   its writable elements and mount the remaining `@Nested` IT scenarios.

## Finish

```bash
./gradlew :app:test --tests '*CertificateSpecConformanceTest'
./gradlew :app:test --tests '*<Type>*'
./gradlew build spotlessCheck
```

If the conformance test reports a difference, the code is wrong. Do not touch
`spec.yaml`.
