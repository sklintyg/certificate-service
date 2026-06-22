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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFormConstants.CHECKED_BOX_VALUE;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;

class PdfConfigurationDateRangeCheckboxTest {

  private static final FieldId RANGE_ID = new FieldId("helt");
  private static final PdfFieldId CHECKBOX = new PdfFieldId("form.ksr[0]");
  private static final PdfFieldId FROM = new PdfFieldId("form.from[0]");
  private static final PdfFieldId TO = new PdfFieldId("form.to[0]");
  private static final LocalDate FROM_DATE = LocalDate.of(2026, 1, 1);
  private static final LocalDate TO_DATE = LocalDate.of(2026, 1, 31);

  @Test
  void shallReturnCheckboxFromAndToFields() {
    final var config =
        PdfConfigurationDateRangeCheckbox.builder().checkbox(CHECKBOX).from(FROM).to(TO).build();
    final var dateRange =
        DateRange.builder().dateRangeId(RANGE_ID).from(FROM_DATE).to(TO_DATE).build();

    final var expected =
        List.of(
            PdfField.builder().fieldId(CHECKBOX).value(CHECKED_BOX_VALUE).build(),
            PdfField.builder().fieldId(FROM).value(FROM_DATE.toString()).build(),
            PdfField.builder().fieldId(TO).value(TO_DATE.toString()).build());

    assertEquals(expected, config.toPdfFields(dateRange).toList());
  }

  @Test
  void shallReturnCheckboxOnlyWhenDatesAreNull() {
    final var config =
        PdfConfigurationDateRangeCheckbox.builder().checkbox(CHECKBOX).from(FROM).to(TO).build();
    final var dateRange = DateRange.builder().dateRangeId(RANGE_ID).from(null).to(null).build();

    final var expected =
        List.of(PdfField.builder().fieldId(CHECKBOX).value(CHECKED_BOX_VALUE).build());

    assertEquals(expected, config.toPdfFields(dateRange).toList());
  }
}
