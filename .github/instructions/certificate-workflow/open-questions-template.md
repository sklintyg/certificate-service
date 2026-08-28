# open-questions.md — the shape

Lives beside the manifest, at
`app/src/test/resources/certificate-specs/<type>/v<M>_<N>/spec.yaml`'s directory.

It exists because the manifest cannot hold a maybe. Every field in `spec.yaml` is either a
value copied from the specification or absent; there is nowhere to write "the document does
not say." That is what this file is for, and it is why the extraction step can obey *never
invent a code, id, or text* without silently dropping the things it could not resolve.

Write it for **the person who can answer it** — usually a requirement owner, not a
developer. An item that can only be understood by reading Java has failed at its job.

## One heading per item, three parts each

```markdown
## <n>. <The question, stated as a fact about the document>

**What the document says.** Quote or cite it. Name the element id, the column, the
section. If the document is silent, say that it is silent — that is itself the finding.

**What is ambiguous.** Why the document does not settle it. Where two readings are both
defensible, give both. Where an existing certificate does something relevant, say which
and what — a precedent is not an answer, but it narrows the question.

**What the implementation needs.** The decision required, and what changes once it
arrives. Name the files or classes affected so the cost of each answer is visible.
```

Number the items and keep the numbers stable — they get cited in commit messages, in
`TODO:` comments and in the conversation with whoever answers them.

State up front which items **block** the manifest or an increment, and which are merely
unresolved. A reader should not have to work out which three of fifteen stop work.

## Resolving an item

Strike the heading through, append `— RESOLVED`, and replace the third part with what was
decided and where it landed:

```markdown
## 1. ~~<the original question>~~ — RESOLVED

**What the document says.** …unchanged…

**What is ambiguous.** …unchanged…

**Resolved.** <The answer, its source, and the commit or file that carries it.>
```

Keep resolved items in place. The reasoning is worth more than the tidiness, and the next
version of the certificate will raise the same question.

## What belongs here

- The **Funktioner** table — whether *ärendekommunikation*, *kompletteringsbegäran* and
  *förnya* are available. These drive the action specification, and `scaffold.md` reads
  this file to decide them.
- The 1177 summary format, when the specification describes one.
- Codes the document references but does not enumerate.
- Any `KKSF-*` or `B-*` code whose meaning is not obvious from an existing implementation.
- Rows the schema cannot represent — a specification row with no Inputkomponent cell.
- Anything a later increment will need but this one must not add. Noticing that a future
  category needs a new code system is a line here, not extra files in this pull request.
- Values a step had to assume because the document is silent. Record the assumption *and*
  the reasoning, so the reviewer can check the reasoning rather than the value.

## What does not belong here

Anything the document actually answers. This file is expensive to read and its authority
comes from every item being real; padding it with questions that a careful second reading
of the specification would settle is how it stops being read.

## Worked example

```markdown
## 7. There are no help texts at all

**What the document says.** The **Hjälptexter** section contains a single "-".

**What is ambiguous.** Whether this means "no help texts for this certificate" or "not
written yet". Every comparable FKASSA certificate has them; FK7804 has help texts on most
elements.

**What the implementation needs.** Confirmation. No `helpText` is set on any element, so
if they arrive later, every affected `Question*` class changes.
```

Note what makes it answerable: it names the section, states the two readings, gives the
precedent, and says what it costs to be wrong. None of that requires the reader to open
the code.
