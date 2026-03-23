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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionIntygetAvser.QUESTION_INTYGET_AVSER_ID;

import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateSummary;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateSummaryProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CheckboxMultipleCodeSummaryValue;

public class TS8071CertificateSummaryProvider implements CertificateSummaryProvider {

  @Override
  public CertificateSummary summaryOf(Certificate certificate) {
    return CertificateSummary.builder()
        .label("Intyget avser")
        .value(
            certificate.signed() != null
                ? CheckboxMultipleCodeSummaryValue.value(QUESTION_INTYGET_AVSER_ID, certificate)
                : "")
        .build();
  }
}
