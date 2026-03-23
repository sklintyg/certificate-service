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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.QuestionFunktionsnedsattning.FUNKTIONSNEDSATTNING_SINNESFUNKTION_ID;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class QuestionSinnesfunktionMotivering extends AbstractFunktionsnedsattningMotivering {

  public static final ElementId FUNKTIONSNEDSATTNING_MOTIVERING_SINNESFUNKTION_ID =
      new ElementId("12");
  private static final FieldId FUNKTIONSNEDSATTNING_MOTIVERING_SINNESFUNKTION_FIELD_ID =
      new FieldId("12.1");
  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].Sida3[0].flt_txtSinnesfunktioner[0]");

  private QuestionSinnesfunktionMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionSinnesfunktionMotivering() {
    return getFunktionsnedsattningMotivering(
        FUNKTIONSNEDSATTNING_MOTIVERING_SINNESFUNKTION_ID,
        FUNKTIONSNEDSATTNING_MOTIVERING_SINNESFUNKTION_FIELD_ID,
        FUNKTIONSNEDSATTNING_SINNESFUNKTION_ID,
        "Sinnesfunktioner och smärta",
        GENERAL_LABEL_FUNKTIONSNEDSATTNING,
        """
            Med sinnesfunktioner och smärta menas exempelvis:
            <ul>
            <li>hörselfunktion som är förmågan att förnimma närvaro av ljud och att urskilja lokalisering, tonhöjd, ljudstyrka och ljudkvalitet.</li><li>synfunktion som är förmåga att förnimma närvaro av ljus och synintryckets form, storlek, utformning och färg.</li><li>känslighet eller upplevelse av obehag vid ljud, ljus, temperatur, beröring, smak eller lukt.</li><li>smärta kan bero på tänkbar eller faktisk skada. Den kan vara generell eller lokal i kroppsdelar, eller i dermatom (hudavsnitt). Smärta kan till exempel vara huggande, brännande eller molande.</li></ul>
            """,
        PDF_FIELD_ID);
  }
}
