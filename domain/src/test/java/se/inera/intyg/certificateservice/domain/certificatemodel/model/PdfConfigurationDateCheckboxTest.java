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
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;

class PdfConfigurationDateCheckboxTest {

  private static final FieldId DATE_ENTRY_ID = new FieldId("undersokning");
  private static final PdfFieldId CHECKBOX_FIELD = new PdfFieldId("form.ksr[0]");
  private static final PdfFieldId DATE_FIELD = new PdfFieldId("form.dat[0]");
  private static final LocalDate DATE = LocalDate.of(2026, 3, 15);

  @Test
  void shallReturnCheckboxAndDateFields() {
    final var config =
        PdfConfigurationDateCheckbox.builder()
            .checkboxFieldId(CHECKBOX_FIELD)
            .dateFieldId(DATE_FIELD)
            .build();
    final var date = ElementValueDate.builder().dateId(DATE_ENTRY_ID).date(DATE).build();

    final var expected =
        List.of(
            PdfField.builder().fieldId(CHECKBOX_FIELD).value(CHECKED_BOX_VALUE).build(),
            PdfField.builder().fieldId(DATE_FIELD).value(DATE.toString()).build());

    assertEquals(expected, config.toPdfFields(date).toList());
  }

  @Test
  void shallReturnEmptyWhenDateIsNull() {
    final var config =
        PdfConfigurationDateCheckbox.builder()
            .checkboxFieldId(CHECKBOX_FIELD)
            .dateFieldId(DATE_FIELD)
            .build();
    final var date = ElementValueDate.builder().dateId(DATE_ENTRY_ID).date(null).build();

    assertEquals(Collections.emptyList(), config.toPdfFields(date).toList());
  }
}
