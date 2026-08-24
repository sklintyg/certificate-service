---
name: cert-schematron
description: "Wire schematron validation and the prefill fixture for a Försäkringskassan certificate."
---

Follow `.github/instructions/certificate-workflow/schematron.md` exactly.

Only FK types have schematron. If the certificate goes to Transportstyrelsen or SKR,
say so and stop.

Pay particular attention to `ElementMapping` on every sub-question — a missing mapping
serialises the element at the wrong level and the schematron rejects it.
