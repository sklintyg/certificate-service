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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.QuestionFunktionsnedsattning.FUNKTIONSNEDSATTNING_ANNAN_KROPPSILIG_FUNKTION_ID;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class QuestionAnnanKroppsligFunktionMotivering
    extends AbstractFunktionsnedsattningMotivering {

  public static final ElementId FUNKTIONSNEDSATTNING_MOTIVERING_ANNAN_KROPPSILIG_FUNKTION_ID =
      new ElementId("14");
  private static final FieldId FUNKTIONSNEDSATTNING_MOTIVERING_ANNAN_KROPPSILIG_FUNKTION_FIELD_ID =
      new FieldId("14.1");
  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].Sida3[0].flt_txtAnnanKroppsligFuntion[0]");

  private QuestionAnnanKroppsligFunktionMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAnnanKroppsligFunktionMotivering() {
    return getFunktionsnedsattningMotivering(
        FUNKTIONSNEDSATTNING_MOTIVERING_ANNAN_KROPPSILIG_FUNKTION_ID,
        FUNKTIONSNEDSATTNING_MOTIVERING_ANNAN_KROPPSILIG_FUNKTION_FIELD_ID,
        FUNKTIONSNEDSATTNING_ANNAN_KROPPSILIG_FUNKTION_ID,
        "Annan kroppslig funktion",
        GENERAL_LABEL_FUNKTIONSNEDSATTNING,
        "Med annan kroppslig funktion menas till exempel andningsfunktion, matsmältnings- och ämnesomsättningsfunktion samt blås- och tarmfunktion.",
        PDF_FIELD_ID);
  }
}
