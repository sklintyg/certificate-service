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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK7210_CERTIFICATE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigMessage;
import se.inera.intyg.certificateservice.application.certificate.dto.config.Message;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;

class CertificateDataMessageConfigConverterTest {

  private CertificateDataMessageConfigConverter certificateDataMessageConfigConverter;

  @BeforeEach
  void setUp() {
    certificateDataMessageConfigConverter = new CertificateDataMessageConfigConverter();
  }

  @Test
  void shouldThrowExceptionIfWrongClass() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(ElementConfigurationDate.builder().build())
            .build();

    assertThrows(
        IllegalStateException.class,
        () ->
            certificateDataMessageConfigConverter.convert(
                elementSpecification, FK7210_CERTIFICATE));
  }

  @Test
  void shouldReturnMessageType() {
    assertEquals(ElementType.MESSAGE, certificateDataMessageConfigConverter.getType());
  }

  @Test
  void shouldReturnConvertedMessage() {
    final var expected =
        CertificateDataConfigMessage.builder()
            .message(
                Message.builder()
                    .content("MESSAGE")
                    .level(
                        se.inera.intyg.certificateservice.application.certificate.dto.config
                            .MessageLevel.INFO)
                    .build())
            .build();

    final var response =
        certificateDataMessageConfigConverter.convert(
            ElementSpecification.builder()
                .configuration(
                    ElementConfigurationMessage.builder()
                        .message(
                            ElementMessage.builder()
                                .content("MESSAGE")
                                .level(MessageLevel.INFO)
                                .build())
                        .build())
                .build(),
            FK7210_CERTIFICATE);

    assertEquals(expected, response);
  }
}
