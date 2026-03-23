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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.QuestionAktivitetsbegransning.AKTIVITETSBAGRENSNINGAR_LARANDE_ID;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class QuestionLarandeBegransningMotivering extends AbstractAktivitetsbegransningMotivering {

  public static final ElementId AKTIVITETSBEGRANSNING_MOTIVERING_LARANDE_BEGRANSNING_ID =
      new ElementId("65");
  private static final FieldId AKTIVITETSBEGRANSNING_MOTIVERING_LARANDE_BEGRANSNING_FIELD_ID =
      new FieldId("65.1");

  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].Sida4[0].flt_txtLarandeKunskap[0]");

  private QuestionLarandeBegransningMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionLarandeBegransningMotivering() {
    return getFunktionsnedsattningMotivering(
        AKTIVITETSBEGRANSNING_MOTIVERING_LARANDE_BEGRANSNING_ID,
        AKTIVITETSBEGRANSNING_MOTIVERING_LARANDE_BEGRANSNING_FIELD_ID,
        AKTIVITETSBAGRENSNINGAR_LARANDE_ID,
        "Lärande, tillämpa kunskap samt allmänna uppgifter och krav",
        GENERAL_LABEL_AKTIVITETSBEGRANSNING,
        "Med lärande och tillämpa kunskap menas att till exempel att förvärva färdigheter, att lösa problem och att fatta beslut. Med allmänna uppgifter och krav menas till exempel att genomföra daglig rutin, eller att hantera stress och andra psykologiska krav.",
        PDF_FIELD_ID);
  }
}
