---
name: cert-pdf
description: "Add PDF configuration for a certificate: the PdfSpecification, the template path provider, and the per-question pdfConfiguration."
---

Follow `.github/instructions/certificate-workflow/pdf.md` and
`.github/instructions/certificate-pdf.instructions.md`.

Decide the strategy first — form-fill, generated layout, or print provider. Only
form-fill involves field ids, and it requires a supplied template and
`<type>_structure.txt`. If either is missing, stop and ask for it rather than inventing
field names.

If a value type has no `Pdf*ValueGenerator`, stop and report it.
