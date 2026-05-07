# Analys: Mappning från FHIR Questionnaire till CertificateModel

## Sammanfattning

Mappningen från en FHIR `Questionnaire` till en `CertificateModel` är **delvis möjlig**. Grundläggande
elementtyper och enkla valideringsregler kan mappas direkt med befintliga domänklasser. Det finns
dock ett antal semantiska gap – framförallt för CertificateModel-fält som är applikationsspecifika
och saknar representation i FHIR-standarden, samt för några FHIR-typer som saknar direkta
domänmotsvarigheter.

---

## 1. Metadata-mappning (Questionnaire → CertificateModel)

| FHIR Questionnaire-fält                           | CertificateModel-fält           | Notering                                                                |
|---------------------------------------------------|---------------------------------|-------------------------------------------------------------------------|
| `identifier[0].value` (t.ex. "FK7804")           | `CertificateModelId.type`       | Direkt mappning via `CertificateType`                                   |
| `version` (t.ex. "2")                             | `CertificateModelId.version`    | Direkt mappning via `CertificateVersion`                                |
| `title` ("Läkarintyg för sjukpenning")            | `name` / `typeName`             | Kan mappas till `name` eller `CertificateTypeName`                      |
| `name` ("Läkarintygförsjukpenning")               | `name`                          | Alternativt till `title`                                                |
| `description`                                     | `description`                   | Direkt mappning                                                         |
| `useContext[purpose].valueCodeableConcept.text`   | `detailedDescription`           | Längre syftestext mappas till detaljerad beskrivning                    |
| `publisher` ("Försäkringskassan")                 | `recipient`                     | Kräver uppslagning till `Recipient`-objekt                              |
| `effectivePeriod.start`                           | `activeFrom`                    | ISO-datumsträng → `LocalDateTime`                                       |
| `status` ("active" / "draft" / "retired")         | `activeFrom` (indirekt)         | Status påverkar aktivitetsbedömning                                     |
| `copyright`                                       | —                               | **Ingen mappning** i CertificateModel                                   |
| `meta.profile`                                    | —                               | **Ingen mappning**                                                      |
| `url`                                             | —                               | **Ingen mappning**                                                      |
| `contact`                                         | —                               | **Ingen mappning** (delvis överlapp med `recipient`)                    |
| `subjectType`                                     | —                               | **Ingen mappning**                                                      |

### CertificateModel-fält utan källa i Questionnaire

Följande fält i `CertificateModel` kan **inte** härledas från en FHIR Questionnaire och måste
tillhandahållas externt, via konfigurationsfiler eller standardvärden:

- `certificateActionSpecifications` – behörighetsregler för åtgärder
- `messageActionSpecifications` – kompletteringsbeteende
- `schematronPath` – XML-valideringssökväg
- `texts` – juridiska texter och meddelanden
- `summaryProvider` – UI-sammanfattningsgenerator
- `pdfSpecification` – PDF-renderingskonfiguration
- `confirmationModalProvider` – bekräftelsedialoger i UI
- `sickLeaveProvider` – sjukskrivningsspecifik logik
- `citizenAvailableFunctionsProvider` – medborgartjänstens funktioner
- `availableForCitizen` – synlighet för medborgare
- `ableToCreateDraftForModel` – version-/modellrelation

---

## 2. Item-mappning (Questionnaire.item → ElementSpecification)

Varje `item` i questionnaire mappas till en `ElementSpecification` med en `ElementConfiguration`.
Nedan redovisas de faktiska items i `questionnaire.json` och deras mappning.

### 2.1 Fullständig item-mappning för questionnaire.json (FK7804 v2)

| linkId (förkortad)  | Fält `text`                                              | FHIR type | itemControl | repeats | Rekommenderad ElementConfiguration         | Kommentar                                                                   |
|---------------------|----------------------------------------------------------|-----------|-------------|---------|---------------------------------------------|-----------------------------------------------------------------------------|
| `bcb1cc19`          | Smittbärarpenning                                        | boolean   | —           | false   | `ElementConfigurationCheckboxBoolean`       | Direkt mappning                                                             |
| `ef8c4650`          | Intyget är baserat på                                    | coding    | check-box   | —       | `ElementConfigurationCheckboxMultipleDate`  | Checkbox med datumfält per val (se avsnitt 3.1)                             |
| `30f23e47`          | I relation till vilken sysselsättning                    | coding    | check-box   | —       | `ElementConfigurationCheckboxMultipleCode`  | Inkl. delfråga `6d6923b6` (string) under "nuvarande arbete"                 |
| `9030f586`          | Diagnos/diagnoser                                        | coding    | drop-down   | true    | `ElementConfigurationDiagnosis` (partiell)  | **Gap** – drop-down + repeats, se avsnitt 3.4                               |
| `92642cf4`          | Funktionsnedsättning                                     | text      | —           | —       | `ElementConfigurationTextArea`              | Direkt mappning                                                             |
| `bdd1ead7`          | Aktivitetsbegränsning                                    | text      | —           | —       | `ElementConfigurationTextArea`              | Direkt mappning                                                             |
| `628bbb1a`          | Medicinsk behandling                                     | text      | —           | —       | `ElementConfigurationTextArea`              | Direkt mappning                                                             |
| `3032f89c`          | Min bedömning av patientens nedsättning av arbetsförmåga | coding    | check-box   | —       | `ElementConfigurationCheckboxDateRangeList` | Checkbox med datumintervall per procentsats (se avsnitt 3.2)                |
| `494d1d31`          | Prognos för arbetsförmåga                                | coding    | check-box   | —       | `ElementConfigurationRadioMultipleCode`     | **Konflikt** – itemControl är check-box men semantiken är radio (avsnitt 3.3) |
| `c569bf18`          | Åtgärder som kan främja återgången i arbete              | text      | —           | —       | `ElementConfigurationTextArea`              | Direkt mappning                                                             |
| `e58b0dbb`          | Övriga upplysningar                                      | text      | —           | —       | `ElementConfigurationTextArea`              | Direkt mappning                                                             |
| `9ef5a5fa`          | Kontakt med Försäkringskassan                            | boolean   | —           | —       | `ElementConfigurationCheckboxBoolean`       | Inkl. delfråga `bdad871e` (string) vid true                                 |

#### Nestade items

| linkId (förkortad) | Förälder      | FHIR type | Rekommenderad ElementConfiguration  | Kommentar                                         |
|--------------------|---------------|-----------|--------------------------------------|---------------------------------------------------|
| `d466a561`         | `ef8c4650`    | date      | Hanteras av `CheckboxMultipleDate`   | Datum för "fysiskt vårdmöte"                      |
| `94fea345`         | `ef8c4650`    | date      | Hanteras av `CheckboxMultipleDate`   | Datum för "digitalt vårdmöte"                     |
| `aef03e3c`         | `ef8c4650`    | date      | Hanteras av `CheckboxMultipleDate`   | Datum för "telefonkontakt"                        |
| `15eb513e`         | `ef8c4650`    | date      | Hanteras av `CheckboxMultipleDate`   | Datum för "journaluppgifter"                      |
| `947b42d9`         | `ef8c4650`    | string    | `ElementConfigurationTextField`      | Fritext för "annat" – **gap**, se avsnitt 3.1     |
| `55257f39`         | `ef8c4650`    | date      | `ElementConfigurationDate`           | Datum för "annat" – **gap**, se avsnitt 3.1       |
| `6d6923b6`         | `30f23e47`    | string    | `ElementConfigurationTextField`      | Yrke/arbetsuppgifter vid "nuvarande arbete"       |
| `868da072`         | `9030f586`    | text/help | Flytta text till förälder (meddelande)| `itemControl: help` → hanteras som `description`  |
| `bc119b74`         | `3032f89c`    | date      | Hanteras av `CheckboxDateRangeList`  | Från-datum 100%                                   |
| `eade6a5d`         | `3032f89c`    | date      | Hanteras av `CheckboxDateRangeList`  | Till-datum 100%                                   |
| *(liknande par)*   | `3032f89c`    | date      | Hanteras av `CheckboxDateRangeList`  | Datum-par för 75%, 50%, 25%                       |
| `1d113389`         | `3032f89c`    | text      | `ElementConfigurationTextArea`       | Motivering vid förlängd sjukskrivning             |
| `fc385131`         | `3032f89c`    | boolean   | `ElementConfigurationCheckboxBoolean`| Transport till arbetsplatsen                      |
| `8df11f41`         | `3032f89c`    | coding/cb | `ElementConfigurationCheckboxMultipleCode` | Ojämn arbetstid vid deltidssjukskrivning    |
| `cc91d31f`         | `8df11f41`    | string    | `ElementConfigurationTextField`      | Medicinska skäl vid "ja"                          |
| `df6d5c66`         | `494d1d31`    | quantity  | `ElementConfigurationInteger`        | Antal månader med unit "mo" (se avsnitt 3.5)      |
| `41b871bd`         | `494d1d31`    | string    | `ElementConfigurationTextField`      | Beskriv grund vid "svårbedömd"                    |
| `bdad871e`         | `9ef5a5fa`    | string    | `ElementConfigurationTextField`      | Anledning till önskad FK-kontakt                  |

---

## 3. Komplexa mönster och gap

### 3.1 Checkbox med mixade undertyper per val (item `ef8c4650`)

**FHIR-struktur:** `coding + check-box` med nested `date`-items per coding-alternativ. Alternativet
"annat" har dessutom en `string`-subfråga utöver en `date`-subfråga.

**Domänmappning:**  
`ElementConfigurationCheckboxMultipleDate` hanterar checkbox med ett datumfält per alternativ
(`ElementValueDateList`). Den kan **inte** nativt hantera ett alternativ med bägge en `date` **och**
en `string`.

**Rekommendation:** Alternativet "annat" med datum + fritext kan hanteras som två separata
`ElementSpecification`-barn (en `date` och en `string`), båda med `enableWhen`-baserade SHOW-regler
som pekar på att "annat" är valt. Annars behöver en ny konfigurationstyp implementeras.

---

### 3.2 Checkbox med datumintervall per alternativ (item `3032f89c`)

**FHIR-struktur:** `coding + check-box` med procentsatser (100%, 75%, 50%, 25%) där varje
procentsats har ett `från-datum` och ett `till-datum` som nestade items.

**Domänmappning:**  
`ElementConfigurationCheckboxDateRangeList` (ElementType `CHECKBOX_DATE_RANGE_LIST`) är exakt
designad för detta mönster och hanterar `ElementValueDateRangeList`. **Direkt mappning möjlig.**

---

### 3.3 Semantisk konflikt: check-box vs. radio (item `494d1d31`)

**FHIR-struktur:** `coding + check-box` för prognos med fyra ömsesidigt uteslutande alternativ.
Logiken kräver exakt ett val (radio-semantik), men itemControl är `check-box`.

**Domänmappning:**  
Domänens `ElementConfigurationRadioMultipleCode` (RADIO_MULTIPLE_CODE) är korrekt för detta
semantiska mönster. Mappningslogiken bör ignorera `check-box` itemControl och istället välja
konfigurationstyp baserat på semantik (max ett val = radio) snarare än enbart FHIR-attribut.

**Rekommendation:** Inför en kompletterande heuristik: om ett `check-box coding`-item saknar
`repeats: true` och alternativen är logiskt ömsesidigt uteslutande → välj
`ElementConfigurationRadioMultipleCode`.

---

### 3.4 Diagnos som drop-down med repeats (item `9030f586`)

**FHIR-struktur:** `coding + drop-down + repeats: true` med fördefinierade `answerOption`
(ICD-10-koder E10, E11). Innebörden är en flervalsdropdown från en fast koddlista.

**Domänmappning:**  
Domänen saknar en konfigurationstyp för "repeating drop-down" / multi-select dropdown från fast
lista. De närmaste alternativen är:

1. `ElementConfigurationDiagnosis` – hanterar diagnoser med terminologiuppslag (ICD-10/ICF), men
   kräver öppen sökning, inte en fast koddlista.
2. `ElementConfigurationCheckboxMultipleCode` – multi-select men renderas som checkboxar, inte
   dropdown.
3. `ElementConfigurationDropdownCode` – dropdown men stöder bara ett enda val.

**Gap:** Ingen befintlig konfigurationstyp täcker `drop-down + repeats: true`. En ny typ
`ElementConfigurationDropdownMultipleCode` (eller liknande) behövs, eller så accepteras
`ElementConfigurationCheckboxMultipleCode` som en visuell kompromiss.

---

### 3.5 Quantity-typ med enhet (item `df6d5c66`)

**FHIR-struktur:** `quantity` med `questionnaire-unitOption` (unit: "mo", "månad").

**Domänmappning:**  
`ElementConfigurationInteger` har fältet `unitOfMeasurement` (String) vilket täcker detta
tillräckligt för heltal. FHIR `quantity` kan innehålla decimalvärden – domänen saknar en
konfigurationstyp för decimaltal. I detta specifika fall (antal månader) är heltal tillräckligt.

---

## 4. Reviderad mappningstabell – FHIR typ till ElementConfiguration

Den ursprungliga mappningstabellen i `questionnaireValueMapping.md` är i stort korrekt men kräver
följande justeringar och tillägg:

| FHIR type      | itemControl  | repeats | Ursprunglig intern typ | Korrekt ElementConfiguration                     | Status   | Kommentar                                                        |
|----------------|--------------|---------|------------------------|--------------------------------------------------|----------|------------------------------------------------------------------|
| boolean        | —            | false   | BOOLEAN                | `ElementConfigurationCheckboxBoolean`            | ✅ Korrekt |                                                                  |
| date           | —            | false   | DATE                   | `ElementConfigurationDate`                       | ✅ Korrekt |                                                                  |
| dateTime       | —            | false   | DATETIME               | —                                                | ❌ Gap   | Ingen `dateTime`-konfigurationstyp. Närmast: `ElementConfigurationDate` (förlorar tid) |
| string         | —            | false   | TEXT                   | `ElementConfigurationTextField`                  | ✅ Korrekt |                                                                  |
| string         | —            | true    | TEXT_LIST              | —                                                | ❌ Gap   | Ingen upprepande textfältstyp i domänen                          |
| text           | —            | false   | TEXTAREA               | `ElementConfigurationTextArea`                   | ✅ Korrekt |                                                                  |
| integer        | —            | false   | NUMBER                 | `ElementConfigurationInteger`                    | ✅ Korrekt |                                                                  |
| decimal        | —            | false   | NUMBER                 | —                                                | ❌ Gap   | Domänen har bara `ElementConfigurationInteger` (heltal)          |
| quantity       | —            | false   | NUMBER_WITH_UNIT       | `ElementConfigurationInteger` (unitOfMeasurement)| ⚠️ Partiell | Fungerar för heltal med enhet, ej decimaler                    |
| coding         | drop-down    | false   | DROPDOWN               | `ElementConfigurationDropdownCode`               | ✅ Korrekt |                                                                  |
| coding         | drop-down    | true    | —                      | —                                                | ❌ Gap   | Ingen multi-select dropdown. Se avsnitt 3.4                      |
| coding         | check-box    | true    | CHECKBOX_GROUP         | `ElementConfigurationCheckboxMultipleCode`       | ✅ Korrekt |                                                                  |
| coding         | check-box    | false   | CHECKBOX_GROUP         | `ElementConfigurationCheckboxMultipleCode` eller `ElementConfigurationRadioMultipleCode` | ⚠️ Partiell | Beror på semantik (avsnitt 3.3)  |
| coding         | radio-button | false   | RADIO_GROUP            | `ElementConfigurationRadioMultipleCode`          | ✅ Korrekt |                                                                  |
| coding         | autocomplete | false   | AUTOCOMPLETE           | `ElementConfigurationIcf` (partiell)             | ⚠️ Partiell | ICF är autocomplete mot terminologi; allmän autocomplete saknas |
| coding         | help         | —       | (utelämna)             | Flytta text till förälder som `description`      | ✅ Korrekt |                                                                  |
| coding         | —            | false   | RADIO_GROUP (default)  | `ElementConfigurationRadioMultipleCode`          | ✅ Korrekt |                                                                  |
| coding         | —            | true    | CHECKBOX_GROUP (default)| `ElementConfigurationCheckboxMultipleCode`      | ✅ Korrekt |                                                                  |
| group          | —            | —       | GROUP                  | `ElementConfigurationCategory`                   | ⚠️ Justering | `GROUP` är inte en `ElementType`; korrekt namn är `CATEGORY`  |
| display        | —            | —       | DISPLAY                | `ElementConfigurationMessage`                    | ⚠️ Justering | `DISPLAY` är inte en `ElementType`; korrekt namn är `MESSAGE`  |
| attachment     | —            | —       | FILE_UPLOAD            | —                                                | ❌ Gap   | Ingen filuppladdningstyp i domänen                               |
| reference      | —            | —       | RESOURCE_PICKER        | —                                                | ❌ Gap   | Ingen resursväljartyp i domänen                                  |

---

## 5. Regelöversättning: enableWhen → ElementRule

FHIR `enableWhen` används genomgående i questionnaire.json för att styra synlighet av delfrågor.

### Mappningsprincip

| FHIR enableWhen-operator | FHIR answerType  | Domän ElementRuleType | Kommentar                                                         |
|--------------------------|------------------|-----------------------|-------------------------------------------------------------------|
| `=` + `answerBoolean`    | boolean          | `SHOW`                | Uttryck: `$questionId = true/false`                               |
| `=` + `answerCoding`     | coding           | `SHOW`                | Uttryck: `$questionId = 'kodvärde'`                               |
| `!=`                     | (valfri)         | `HIDE`                | Uttryck med negation                                              |
| `exists`                 | (valfri)         | `SHOW`                | Uttryck: kontrollera att svar finns                               |

Alla `enableWhen`-regler i questionnaire.json använder `operator: "="` med antingen
`answerBoolean` eller `answerCoding`. Dessa kan mappas till `ElementRuleExpression` med
`ElementRuleType.SHOW`.

**`required: true`** mappar till `ElementRuleExpression` med typ `MANDATORY` eller `CATEGORY_MANDATORY`.

---

## 6. Fält-ID-hantering

FHIR Questionnaire använder `linkId` (UUID-format, t.ex. `bcb1cc19-83ce-410f-8551-79abd301e424`) som
unik identifierare per item. Domänens `ElementId` och `FieldId` är enkla String-wrappers och kan
direkt ta emot ett `linkId` som värde.

**Rekommendation:** Använd `linkId` direkt som `ElementId` och `FieldId` för att bevara referenserna
i `enableWhen`-regler utan extra transformationssteg.

---

## 7. Kodlistor och `answerOption`

FHIR `answerOption[].valueCoding` innehåller:
- `id` – UUID (kan användas som `ElementConfigurationCode.id()` / `FieldId`)
- `system` – URN (kan användas som `Code.codeSystem()`)
- `code` – Textkod (kan användas som `Code.code()`)
- `display` – Visningstext (kan användas som `Code.displayName()`)

Dessa fält mappar väldefinierat till domänens `Code`-objekt och `ElementConfigurationCode`.

---

## 8. Markdown-text och `_text`-extensions

Questionnaire-items använder `_text.extension[rendering-markdown]` för formaterad text med markdown.
Domänens konfigurationsklasser har `name`, `description`, `label` och `header` som plain Strings
och stöder inte markdown nativt.

**Gap:** Om markdown ska bevaras behövs antingen konvertering till plaintext eller stöd för
markdown i domänens texttfält.

Exemplet på `SHCSublabelExtension` (url: `...SHCSublabelExtension`, valueMarkdown) används som
undertextsbeskrivning och bör mappas till `description`-fältet i konfigurationsklassen.

---

## 9. Saknade fält i questionnaire.json (jämfört med full CertificateModel)

Questionnaire som källa är designad för **datasamling** (frågor och svar) och saknar per design
information om:

- **Behörighet** – vem får skapa, signera, läsa intyget
- **PDF-rendering** – sidlayout, fältpositioner, sidhuvud/sidfot
- **Skicka/ta emot** – mottagarintegration med Försäkringskassan
- **Arv/version** – relationen till föregående version av intygsmodellen
- **Meddelanden** – kompletteringsflöden
- **Schematron** – XML-valideringslogik

Dessa delar måste tillhandahållas via ett kompletterande konfigurationslager utanför Questionnaire.

---

## 10. Föreslagen arkitektur för mappningsimplementation

```
FHIRIntegrationService.getQuestionnaire()
        │
        ▼
QuestionnaireToCertificateModelMapper
 ├── mapMetadata()       → CertificateModelId, name, description, activeFrom, recipient
 ├── mapItems()          → List<ElementSpecification>
 │    ├── ItemTypeResolver        (FHIR type + itemControl + repeats → ElementConfiguration)
 │    ├── EnableWhenRuleMapper    (enableWhen → ElementRuleExpression SHOW/HIDE)
 │    ├── RequiredRuleMapper      (required → ElementRuleExpression MANDATORY)
 │    └── AnswerOptionMapper      (answerOption → List<ElementConfigurationCode>)
 └── applyExternalConfig()  → actions, pdf, schematron etc. (från separat konfiguration)
```

---

## 11. Slutsats och rekommendationer

### Möjlig mappning ✅
Följande typer och mönster kan mappas direkt med befintliga domänklasser:
- `boolean`, `date`, `string`, `text` → enkla konfigurationstyper
- `coding` med `check-box`, `radio-button`, `drop-down` (ej repeats) → existerande kodkonfigurationer
- `coding + check-box` med datumintervall per alternativ → `ElementConfigurationCheckboxDateRangeList`
- `coding + check-box` med datumfält per alternativ → `ElementConfigurationCheckboxMultipleDate`
- `quantity` med heltalsenheter → `ElementConfigurationInteger`
- `enableWhen` med `= true/false/coding` → `ElementRuleExpression SHOW`

### Kräver ny eller utökad typ ⚠️
- `coding + drop-down + repeats: true` → Ny `ElementConfigurationDropdownMultipleCode` behövs
- `check-box` alternativ med blandade undertyper (date + string) → Ny sammansatt typ eller hantera som separata children

### Saknar stöd i domänmodellen ❌
- `dateTime` – ingen tidskomponent i domänens datumstyp
- `string + repeats: true` – ingen upprepande textfältstyp
- `decimal` – bara heltalsstöd
- `attachment` (filuppladdning) – saknas helt
- `reference` (resurspekare) – saknas helt

### Kräver kompletterande konfiguration (utanför Questionnaire)
Alla applikationsspecifika fält i `CertificateModel` (behörighet, PDF, Schematron, meddelanden,
medborgartjänst m.fl.) måste tillföras från en separat källa vid sidan av Questionnaire.
