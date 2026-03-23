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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionPeriodBedomning.QUESTION_PERIOD_BEDOMNING_ID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateSummary;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class AG114CertificateSummaryProviderTest {

  private static final LocalDateTime SIGNED = LocalDateTime.now();
  private static final LocalDate FROM = LocalDate.now();
  private static final LocalDate TO = LocalDate.now().plusDays(7);
  private static final FieldId FIELD_ID = new FieldId("7.2");

  @Test
  void shouldIncludeCertificateSummary() {
    final var elementValue =
        ElementValueDateRange.builder().fromDate(FROM).toDate(TO).id(FIELD_ID).build();
    final var certificate =
        MedicalCertificate.builder()
            .signed(SIGNED)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_PERIOD_BEDOMNING_ID)
                        .value(elementValue)
                        .build()))
            .build();

    final var expected =
        CertificateSummary.builder()
            .label("Gäller intygsperiod")
            .value(
                FROM.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + " - "
                    + TO.format(DateTimeFormatter.ISO_LOCAL_DATE))
            .build();

    assertEquals(expected, new AG114CertificateSummaryProvider().summaryOf(certificate));
  }

  @Test
  void shouldReturnEmptyValueWhenCertificateIsNotSigned() {
    final var elementValue =
        ElementValueDateRange.builder().fromDate(FROM).toDate(TO).id(FIELD_ID).build();
    final var certificate =
        MedicalCertificate.builder()
            .signed(null)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_PERIOD_BEDOMNING_ID)
                        .value(elementValue)
                        .build()))
            .build();

    final var expected =
        CertificateSummary.builder().label("Gäller intygsperiod").value("").build();

    assertEquals(expected, new AG114CertificateSummaryProvider().summaryOf(certificate));
  }

  @Test
  void shouldReturnEmptyValueWhenElementDataIsMissing() {
    final var certificate =
        MedicalCertificate.builder().signed(SIGNED).elementData(List.of()).build();

    final var expected =
        CertificateSummary.builder().label("Gäller intygsperiod").value("").build();

    assertEquals(expected, new AG114CertificateSummaryProvider().summaryOf(certificate));
  }

  @Test
  void shouldReturnEmptyValueWhenFromDateIsNull() {
    final var elementValue =
        ElementValueDateRange.builder().fromDate(null).toDate(TO).id(FIELD_ID).build();
    final var certificate =
        MedicalCertificate.builder()
            .signed(SIGNED)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_PERIOD_BEDOMNING_ID)
                        .value(elementValue)
                        .build()))
            .build();

    final var expected =
        CertificateSummary.builder().label("Gäller intygsperiod").value("").build();

    assertEquals(expected, new AG114CertificateSummaryProvider().summaryOf(certificate));
  }

  @Test
  void shouldReturnEmptyValueWhenToDateIsNull() {
    final var elementValue =
        ElementValueDateRange.builder().fromDate(FROM).toDate(null).id(FIELD_ID).build();
    final var certificate =
        MedicalCertificate.builder()
            .signed(SIGNED)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_PERIOD_BEDOMNING_ID)
                        .value(elementValue)
                        .build()))
            .build();

    final var expected =
        CertificateSummary.builder().label("Gäller intygsperiod").value("").build();

    assertEquals(expected, new AG114CertificateSummaryProvider().summaryOf(certificate));
  }

  @Test
  void shouldThrowExceptionWhenElementValueIsNotDateRange() {
    final var elementValue =
        ElementValueText.builder().textId(FIELD_ID).text("Not a date range").build();
    final var certificate =
        MedicalCertificate.builder()
            .signed(SIGNED)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_PERIOD_BEDOMNING_ID)
                        .value(elementValue)
                        .build()))
            .build();

    assertThrows(
        IllegalStateException.class,
        () -> new AG114CertificateSummaryProvider().summaryOf(certificate));
  }

  @Test
  void shouldFormatDatesCorrectly() {
    final var fromDate = LocalDate.of(2025, 10, 6);
    final var toDate = LocalDate.of(2025, 10, 13);
    final var elementValue =
        ElementValueDateRange.builder().fromDate(fromDate).toDate(toDate).id(FIELD_ID).build();
    final var certificate =
        MedicalCertificate.builder()
            .signed(SIGNED)
            .elementData(
                List.of(
                    ElementData.builder()
                        .id(QUESTION_PERIOD_BEDOMNING_ID)
                        .value(elementValue)
                        .build()))
            .build();

    final var expected =
        CertificateSummary.builder()
            .label("Gäller intygsperiod")
            .value("2025-10-06 - 2025-10-13")
            .build();

    assertEquals(expected, new AG114CertificateSummaryProvider().summaryOf(certificate));
  }
}
