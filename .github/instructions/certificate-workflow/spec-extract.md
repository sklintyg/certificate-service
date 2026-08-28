# Step 0 — specification → manifest

**Input:** the specification PDF ("Intygsspecifikation").
**Output:** `app/src/test/resources/certificate-specs/<type>/v<major>_<minor>/spec.yaml`
and, beside it, `open-questions.md`. No Java.

This is the only step that reads the PDF. Everything downstream reads the manifest,
so an error here propagates everywhere — and a human reviewing this one file against
the document is the cheapest review in the whole process.

## Getting the text out

The specification is a Google Docs export: subset-embedded fonts, text recoverable
only through the ToUnicode maps. Tools that ignore those produce mojibake.

- **IntelliJ / VS Code Copilot chat:** attach the PDF directly.
- **Copilot CLI / Claude Code:** convert first. `pdftotext -layout` preserves the
  table columns, which matters more than anything else here:
  ```bash
  brew install poppler     # once
  pdftotext -layout "IT-<TYPE> Intygsspecifikation ....pdf" spec.txt
  ```
  Without `-layout` the table columns interleave and rows become unreadable.

## What the document contains

| Section | Goes to |
|---|---|
| Header table (Intygets namn / version / kod) | `certificate:` |
| **Specifikation** — the main table | `categories:` and their `elements:` |
| **Texter → Text innan val av intyg** | `certificate.description` |
| **Texter → Text efter val av intyg** | `certificate.detailedDescription` |
| **Hjälptexter** | each element's `helpText` |
| **Funktioner** | `open-questions.md` — these drive the action specification, not the model |
| **Webcert - specifika krav** | `open-questions.md` |

The main table's columns map straight onto element fields:

| Column | Field |
|---|---|
| ID | `id` — used verbatim as the `ElementId` |
| Typ | `kind` — `KAT` starts a new category, `FRG` / `DFR` are elements |
| Inputkomponent (datatyp) | `component` — e.g. `SK-002 (kodverk)`, or a `KKSF-*` code for a combined component |
| Multiplicitet | `multiplicity` |
| Regel | `rules` — the `SR-*` codes; `B-*` codes go to `open-questions.md` |
| Rubriktext | `label`, plus `header` / `checkboxLabel` when the cell names them |
| XML-mappning | `fieldId` from `TextId` / `BooleskId` / `KodId` / `DatumId` / `PeriodKodId`; `codeSystem` from `Kodverk`; `options` from `Urval` |

## Rules

1. **Copy texts character for character.** Punctuation, casing, `<b>`/`<ul>` markup,
   interior line breaks. Use a YAML block scalar (`|-`) for anything multi-line.
   Trailing whitespace is the one thing normalised away.
2. **Categories get `KAT_1`, `KAT_2`, …** numbered in the order they appear. The
   specification gives them no id.
3. **A `DFR` row sets `parent`** to the `FRG` (or `DFR`) it sits under. Read this from
   the rule cell — `FrågeId: 28.1, Värde: NUVARANDE_ARBETE` means the parent is 28.
4. **`options` are in display order** — the order of the options column, which is not
   always the order inside the `Urval: [...]` bracket. The bracket is a set; the
   column is the order the doctor sees.
5. **`status: pending` for every element.** Only the increment that implements an
   element flips it.
6. **Every element gets a row, including ones you are unsure about.** Unknowns go in
   `open-questions.md`, not into a guess and not into omission.

   One exception, forced by the schema: `component` is required, so a specification row
   with an empty Inputkomponent cell cannot be represented at all. Those rows are
   structural sub-ids of a combined component — the code and date halves of a
   `KKSF-006a`, or the code and description halves of a `KKSF-001` — and belong to the
   parent element, whose `fieldId` names one of them. Leave them out of `categories:`
   and record what you left out, and why, in `open-questions.md`.
7. **Do not write the "Vårdenhetens adress" row.** It is `issuingUnitContactInfo()`,
   shared by every model, and is not part of the manifest.

## open-questions.md

One heading per unresolved item, each stating what the document says, what is
ambiguous, and what the implementation would need. Typical contents: the **Funktioner**
table (whether *ärendekommunikation*, *kompletteringsbegäran*, *förnya* are available),
the 1177 summary format, codes the document references but does not enumerate, and any
`KKSF-*` rule whose meaning is not obvious from an existing implementation.

Follow `open-questions-template.md` for the shape. Write each item so a requirement
owner can answer it without reading any Java — this file is what goes to the person
who can settle the question, and it is the reason the manifest can be honest about
what the document does not say.

## Finish

```bash
./gradlew :app:test --tests '*CertificateSpecConformanceTest'
```

`manifestShouldBeWellFormed` runs on every manifest and will reject an unknown
`component` or `rules` code. It will also fail with "No model registered" — that is
expected until `cert-scaffold` runs, and is the signal that the manifest is in place.

Then hand the pull request to a human with: "review `spec.yaml` against the PDF; no
Java in this change."
