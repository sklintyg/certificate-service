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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.QuestionFunktionsnedsattning.FUNKTIONSNEDSATTNING_INTELLEKTUELL_FUNKTION_ID;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class QuestionIntellektuellFunktionMotivering
    extends AbstractFunktionsnedsattningMotivering {

  public static final ElementId FUNKTIONSNEDSATTNING_MOTIVERING_INTELLEKTUELL_FUNKTION_ID =
      new ElementId("8");
  private static final FieldId FUNKTIONSNEDSATTNING_MOTIVERING_INTELLEKTUELL_FUNKTION_FIELD_ID =
      new FieldId("8.1");
  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].Sida2[0].flt_txttModul_4A[0]");

  private QuestionIntellektuellFunktionMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionIntellektuellFunktionMotivering() {
    return getFunktionsnedsattningMotivering(
        FUNKTIONSNEDSATTNING_MOTIVERING_INTELLEKTUELL_FUNKTION_ID,
        FUNKTIONSNEDSATTNING_MOTIVERING_INTELLEKTUELL_FUNKTION_FIELD_ID,
        FUNKTIONSNEDSATTNING_INTELLEKTUELL_FUNKTION_ID,
        "Intellektuell funktion",
        "Beskriv eventuella iakttagelser alternativt testresultat från psykologutredning",
        "Med intellektuell funktion, teoretisk begåvning eller intelligens menas förmågan att tänka logiskt. För att bedöma nivån krävs det att test utförts av en psykolog.",
        PDF_FIELD_ID);
  }
}
