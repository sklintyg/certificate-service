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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.v1_1;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificatePrintTagProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.FK3226PrintTagKey;

public class FK3226V1_1PrintTagProvider implements CertificatePrintTagProvider {

  @Override
  public PdfFieldId fieldId(Enum<?> key) {
    return switch ((FK3226PrintTagKey) key) {
      case QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_DATE ->
          new PdfFieldId("form1[0].#subform[1].flt_datumPatientensOverlevnad[0]");
      case QUESTION_NAR_TILLSTANDET_BLEV_AKUT_LIVSHOTANDE_DATE ->
          new PdfFieldId("form1[0].#subform[1].flt_datumTillstand[0]");
      case QUESTION_UTLATANDE_BASERAT_PA_INVESTIGATION_DATE ->
          new PdfFieldId("form1[0].#subform[0].flt_datumUnsersokning[0]");
      case QUESTION_UTLATANDE_BASERAT_PA_JOURNAL_DATE ->
          new PdfFieldId("form1[0].#subform[0].flt_datumJournaluppgifter[0]");
      case QUESTION_UTLATANDE_BASERAT_PA_OTHER_DATE ->
          new PdfFieldId("form1[0].#subform[0].flt_datumAnnat[0]");
    };
  }
}
