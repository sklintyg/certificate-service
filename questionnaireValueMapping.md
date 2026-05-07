| FHIR type        | itemControl        | repeats | Intern inputType                 |
|------------------|--------------------|---------|----------------------------------|
| boolean          | —                  | false   | BOOLEAN                          |
| date             | —                  | false   | DATE                             |
| dateTime         | —                  | false   | DATETIME                         |
| string           | —                  | false   | TEXT                             |
| string           | —                  | true    | TEXT_LIST                       |
| text             | —                  | false   | TEXTAREA                         |
| integer / decimal| —                  | false   | NUMBER                           |
| quantity         | —                  | false   | NUMBER_WITH_UNIT                |
| coding           | drop-down          | false   | DROPDOWN                         |
| coding           | check-box          | true    | CHECKBOX_GROUP                  |
| coding           | radio-button       | false   | RADIO_GROUP                     |
| coding           | autocomplete       | false   | AUTOCOMPLETE                    |
| coding           | —                  | false   | RADIO_GROUP (default)           |
| coding           | —                  | true    | CHECKBOX_GROUP (default)        |
| coding           | help               | —       | (utelämna, flytta text till parent) |
| group            | —                  | —       | GROUP                            |
| display          | —                  | —       | DISPLAY                          |
| attachment       | —                  | —       | FILE_UPLOAD                     |
| reference        | —                  | —       | RESOURCE_PICKER                 |