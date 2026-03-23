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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.xml.bind.JAXBElement;
import java.time.LocalDate;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;

@ExtendWith(MockitoExtension.class)
class XmlGeneratorDateRangeTest {

  private static final String QUESTION_ID = "QUESTION_ID";
  private static final LocalDate FROM = LocalDate.now().minusDays(1);
  private static final LocalDate TO = LocalDate.now();

  @InjectMocks private XmlGeneratorDateRange xmlGenerator;

  private ElementData data;
  private ElementSpecification elementSpecification;

  @BeforeEach
  void setup() {
    data =
        ElementData.builder()
            .id(new ElementId(QUESTION_ID))
            .value(
                ElementValueDateRange.builder()
                    .id(new FieldId("ID"))
                    .fromDate(FROM)
                    .toDate(TO)
                    .build())
            .build();

    elementSpecification = ElementSpecification.builder().build();
  }

  @Test
  void shouldMapSvar() {
    final var response = xmlGenerator.generate(data, elementSpecification);

    final var first = response.getFirst();
    assertAll(
        () -> assertEquals(1, response.size()), () -> assertEquals(QUESTION_ID, first.getId()));
  }

  @Test
  void shouldMapDelsvarForDateRange() {
    final var response = xmlGenerator.generate(data, elementSpecification);

    final var delsvar = response.getFirst().getDelsvar().getFirst();
    final var jaxbElement = (JAXBElement<DatePeriodType>) delsvar.getContent().getFirst();
    final var dateRange = jaxbElement.getValue();

    assertAll(
        () -> assertEquals(FROM, toLocalDate(dateRange.getStart())),
        () -> assertEquals(TO, toLocalDate(dateRange.getEnd())));
  }

  @Test
  void shouldMapEmptyIfNoValue() {
    final var data =
        ElementData.builder()
            .id(new ElementId(QUESTION_ID))
            .value(ElementValueDateRange.builder().build())
            .build();

    final var response = xmlGenerator.generate(data, elementSpecification);

    assertTrue(response.isEmpty());
  }

  @Test
  void shouldMapEmptyIfValueIsNotDateRange() {
    final var data = ElementData.builder().id(new ElementId(QUESTION_ID)).value(null).build();

    final var response = xmlGenerator.generate(data, elementSpecification);

    assertTrue(response.isEmpty());
  }

  private static LocalDate toLocalDate(XMLGregorianCalendar xmlFormat) {
    return LocalDate.of(xmlFormat.getYear(), xmlFormat.getMonth(), xmlFormat.getDay());
  }
}
