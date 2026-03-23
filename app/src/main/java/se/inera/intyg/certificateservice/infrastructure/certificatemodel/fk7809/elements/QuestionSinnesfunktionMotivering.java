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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7809.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7809.elements.QuestionFunktionsnedsattning.FUNKTIONSNEDSATTNING_SINNESFUNKTION_ID;

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
      new PdfFieldId("form1[0].#subform[2].flt_txtIntellektuellFunktion[6]");

  private QuestionSinnesfunktionMotivering() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionSinnesfunktionMotivering() {
    return getFunktionsnedsattningMotivering(
        FUNKTIONSNEDSATTNING_MOTIVERING_SINNESFUNKTION_ID,
        FUNKTIONSNEDSATTNING_MOTIVERING_SINNESFUNKTION_FIELD_ID,
        FUNKTIONSNEDSATTNING_SINNESFUNKTION_ID,
        "Övriga sinnesfunktioner och smärta",
        GENERAL_LABEL_FUNKTIONSNEDSATTNING,
        "Med övriga sinnesfunktioner menas exempelvis känslighet eller upplevelse av obehag vid ljud, ljus, temperatur, beröring, smak eller lukt. Med smärta menas förnimmelse av en obehaglig känsla som tyder på tänkbar eller faktisk skada i någon del av kroppen. Det innefattar förnimmelser av generell eller lokal smärta i en eller flera kroppsdelar, eller i ett dermatom (hudavsnitt). Det kan till exempel vara huggande, brännande, molande smärta och värk.",
        PDF_FIELD_ID);
  }
}
