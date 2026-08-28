---
name: cert-version-diff
description: "Plan a new version of an existing certificate: lock the current version, then decide element by element what is common, new or removed."
---

Follow `.github/instructions/certificate-workflow/version-diff.md` exactly.

Compare **by id**, never by class or file name.

The version-lock pull request comes first and lands on its own. Refactoring elements
into `common/` before the previous version is locked makes the refactor unverifiable.

Be strict about what "common" means: every text, help text, component, limit, rule,
validation and code identical. One changed character makes it a new version-specific
element. Where you are unsure, mark it version-specific — a duplicated class is cheap,
a wrongly shared one silently changes a released certificate.
