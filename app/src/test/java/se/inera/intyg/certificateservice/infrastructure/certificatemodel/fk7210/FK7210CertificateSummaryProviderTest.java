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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_ID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateSummary;

class FK7210CertificateSummaryProviderTest {

  private static final LocalDateTime SIGNED = LocalDateTime.now();
  private static final LocalDate DATE = LocalDate.now().plusMonths(1);

  @Test
  void shallIncludeCertificateSummary() {
    final var certificate =
        fk7210CertificateBuilder()
            .signed(SIGNED)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_BERAKNAT_FODELSEDATUM_ID)
                        .value(ElementValueDate.builder().date(DATE).build())
                        .build()))
            .build();

    final var certificateSummary =
        CertificateSummary.builder()
            .label("Gäller intygsperiod")
            .value(
                SIGNED.format(DateTimeFormatter.ISO_DATE)
                    + " - "
                    + DATE.format(DateTimeFormatter.ISO_DATE))
            .build();

    assertEquals(certificateSummary, new FK7210CertificateSummaryProvider().summaryOf(certificate));
  }
}
