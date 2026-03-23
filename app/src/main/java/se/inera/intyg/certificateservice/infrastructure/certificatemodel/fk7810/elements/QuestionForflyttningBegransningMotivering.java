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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.QuestionAktivitetsbegransning.AKTIVITETSBAGRENSNINGAR_MOVEMENT_ID;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class QuestionForflyttningBegransningMotivering
    extends AbstractAktivitetsbegransningMotivering {

  public static final ElementId AKTIVITETSBEGRANSNING_MOTIVERING_FORFLYTTNING_BEGRANSNING_ID =
      new ElementId("67");
  private static final FieldId AKTIVITETSBEGRANSNING_MOTIVERING_FORFLYTTNING_BEGRANSNING_FIELD_ID =
      new FieldId("67.1");
  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].Sida4[0].flt_txtForflyttning[0]");

  private QuestionForflyttningBegransningMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionForflyttningBegransningMotivering() {
    return getFunktionsnedsattningMotivering(
        AKTIVITETSBEGRANSNING_MOTIVERING_FORFLYTTNING_BEGRANSNING_ID,
        AKTIVITETSBEGRANSNING_MOTIVERING_FORFLYTTNING_BEGRANSNING_FIELD_ID,
        AKTIVITETSBAGRENSNINGAR_MOVEMENT_ID,
        "Förflyttning",
        GENERAL_LABEL_AKTIVITETSBEGRANSNING,
        "Med förflyttning menas till exempel att röra sig genom att ändra kroppsställning, att förflytta sig och att använda handens finmotorik för att flytta föremål.",
        PDF_FIELD_ID);
  }
}
