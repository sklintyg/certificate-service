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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.QuestionFunktionsnedsattning.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SOCIAL_INTERAKTION_ID;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class QuestionKommunikationSocialInteraktionMotivering
    extends AbstractFunktionsnedsattningMotivering {

  public static final ElementId
      FUNKTIONSNEDSATTNING_MOTIVERING_KOMMUNIKATION_SOCIAL_INTERAKTION_ID = new ElementId("9");
  private static final FieldId
      FUNKTIONSNEDSATTNING_MOTIVERING_KOMMUNIKATION_SOCIAL_INTERAKTION_FIELD_ID =
          new FieldId("9.1");

  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].Sida2[0].flt_txttModul_4A[1]");

  private QuestionKommunikationSocialInteraktionMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionKommunikationSocialInteraktionMotivering() {
    return getFunktionsnedsattningMotivering(
        FUNKTIONSNEDSATTNING_MOTIVERING_KOMMUNIKATION_SOCIAL_INTERAKTION_ID,
        FUNKTIONSNEDSATTNING_MOTIVERING_KOMMUNIKATION_SOCIAL_INTERAKTION_FIELD_ID,
        FUNKTIONSNEDSATTNING_KOMMUNIKATION_SOCIAL_INTERAKTION_ID,
        "Övergripande psykosociala funktioner",
        "Beskriv funktionsnedsättningen, om möjligt med grad. Ange även eventuella undersökningsfynd.",
        """
            Med psykosociala funktioner menas
            <ul>
            <li>förmågan till emotionell kontakt</li><li>social ömsesidighet</li><li>samspel</li><li>förmågan att på ett teoretiskt plan kunna sätta sig in i hur andra människor tänker och känner, även om man inte har varit med om samma sak själv.</li></ul>
            """,
        PDF_FIELD_ID);
  }
}
