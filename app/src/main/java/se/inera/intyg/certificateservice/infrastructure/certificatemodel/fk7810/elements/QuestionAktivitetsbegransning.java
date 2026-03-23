/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemAktivitetsbegransning.FORFLYTTNING_BEGRANSNING;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemAktivitetsbegransning.KOMMUNIKATION_BEGRANSNING;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemAktivitetsbegransning.LARANDE_BEGRANSNING;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemAktivitetsbegransning.OVRIGA_BEGRANSNING;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemAktivitetsbegransning.PERSONLIG_VARD_BEGRANSNING;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationHidden;
import se.inera.intyg.certificateservice.domain.common.model.Code;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCodeList;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionAktivitetsbegransning {

  public static final ElementId AKTIVITETSBAGRENSNINGAR_ID = new ElementId("aktivitetsbegransning");
  public static final FieldId AKTIVITETSBAGRENSNINGAR_FIELD_ID =
      new FieldId("aktivitetsbegransning");
  public static final FieldId AKTIVITETSBAGRENSNINGAR_LARANDE_ID = new FieldId("15.2");
  public static final FieldId AKTIVITETSBAGRENSNINGAR_KOMMUNIKATION_ID = new FieldId("16.2");
  public static final FieldId AKTIVITETSBAGRENSNINGAR_MOVEMENT_ID = new FieldId("17.2");
  public static final FieldId AKTIVITETSBAGRENSNINGAR_PERSONAL_CARE_ID = new FieldId("18.2");
  public static final FieldId AKTIVITETSBAGRENSNINGAR_OVRIG_ID = new FieldId("19.2");

  private QuestionAktivitetsbegransning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAktivitetsbegransning() {
    final var checkboxes =
        List.of(
            getCodeConfig(AKTIVITETSBAGRENSNINGAR_LARANDE_ID, LARANDE_BEGRANSNING),
            getCodeConfig(AKTIVITETSBAGRENSNINGAR_KOMMUNIKATION_ID, KOMMUNIKATION_BEGRANSNING),
            getCodeConfig(AKTIVITETSBAGRENSNINGAR_MOVEMENT_ID, FORFLYTTNING_BEGRANSNING),
            getCodeConfig(AKTIVITETSBAGRENSNINGAR_PERSONAL_CARE_ID, PERSONLIG_VARD_BEGRANSNING),
            getCodeConfig(AKTIVITETSBAGRENSNINGAR_OVRIG_ID, OVRIGA_BEGRANSNING));

    return ElementSpecification.builder()
        .id(AKTIVITETSBAGRENSNINGAR_ID)
        .includeInXml(false)
        .includeForCitizen(false)
        .configuration(
            ElementConfigurationCheckboxMultipleCode.builder()
                .id(AKTIVITETSBAGRENSNINGAR_FIELD_ID)
                .name("Välj alternativ för att visa fritextfält. Välj minst ett:")
                .elementLayout(ElementLayout.COLUMNS)
                .list(checkboxes)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    AKTIVITETSBAGRENSNINGAR_ID,
                    List.of(
                        AKTIVITETSBAGRENSNINGAR_LARANDE_ID,
                        AKTIVITETSBAGRENSNINGAR_KOMMUNIKATION_ID,
                        AKTIVITETSBAGRENSNINGAR_MOVEMENT_ID,
                        AKTIVITETSBAGRENSNINGAR_PERSONAL_CARE_ID,
                        AKTIVITETSBAGRENSNINGAR_OVRIG_ID))))
        .validations(List.of(ElementValidationCodeList.builder().mandatory(true).build()))
        .pdfConfiguration(PdfConfigurationHidden.builder().build())
        .build();
  }

  private static ElementConfigurationCode getCodeConfig(FieldId fieldId, Code code) {
    return new ElementConfigurationCode(fieldId, code.displayName(), code);
  }
}
