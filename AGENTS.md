# certificate-service — agent instructions

Spring Boot / Gradle multi-module service that manages medical certificates
(*intyg*) issued by health professionals. Domain-Driven Design: `domain/` holds the
model and business rules, `app/` holds the concrete certificate implementations and
all adapters.

## Modules

| Module | Contains |
|---|---|
| `domain` | Certificate domain model, `ElementSpecification`, validation, actions |
| `app` | Spring app, REST API, persistence, **all concrete certificate models**, testability |
| `clinicalprocess-certificate-v4` | XML generation, prefill, schema/schematron validation |
| `pdfbox-generator` | Form-fill PDF generation (AcroForm templates) |
| `integration-certificate-print-service` | Generated-layout PDF via the print service |
| `integration-test` | Spring Boot + Testcontainers integration tests (`integrationTest` task) |

## Commands

```bash
./gradlew build spotlessCheck        # unit tests + formatting (what CI runs first)
./gradlew :app:test --tests '*Foo*'  # a single unit test
./gradlew integrationtest            # the integration-test module (Testcontainers)
./gradlew spotlessApply              # fix formatting
```

Formatting is enforced: google-java-format, licence header from
`spotless.license.txt`, no wildcard imports. Always run `spotlessApply` before
finishing.

## Coding conventions

@.github/copilot-instructions.md

## Working on certificate models

**Read this before touching anything under `certificatemodel/`.** Implementing a
certificate model is a long, detail-dense transcription job, and it is done as a
sequence of small merged increments — never in one shot.

@.github/instructions/certificate-workflow/README.md

Each step has an agent. In Copilot CLI pick one with `/agent`; in Claude Code they are
skills of the same name.

| Agent | Step |
|---|---|
| `cert-spec-extract` | specification PDF → reviewed `spec.yaml` manifest |
| `cert-scaffold` | the model skeleton, no categories yet |
| `cert-category` | one category, one pull request |
| `cert-schematron` | XML validation and the prefill fixture |
| `cert-pdf` | PDF specification and per-question configuration |
| `cert-verify` | audit the code against the manifest |
| `cert-version-diff` | a new version of an existing certificate |

Reference material, loaded automatically by path:

- `.github/instructions/certificate-model.instructions.md` — model & element anatomy
- `.github/instructions/certificate-elements.instructions.md` — spec code → Java type tables
- `.github/instructions/certificate-tests.instructions.md` — test patterns at every layer
- `.github/instructions/certificate-pdf.instructions.md` — the three PDF strategies

## Hard rules

1. **Never invent a code, id, or text.** If the specification does not give you a
   value, emit a constant with a `TODO:` comment and record it in the spec
   manifest's `open-questions.md`. Do not guess.
2. **Texts are copied word for word** from the specification — including
   punctuation, casing and line breaks. These strings are shown to doctors and are
   legally significant.
3. **Every increment must leave `main` green.** If you cannot finish a category,
   leave it out of the model rather than committing a half-wired one.
