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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.message.dto.QuestionTypeDTO;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateMessageType;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;
import se.inera.intyg.certificateservice.domain.message.model.Subject;

class CertificateMessageTypeConverterTest {

  private static final Subject SUBJECT = new Subject("subject");

  private CertificateMessageTypeConverter certificateMessageTypeConverter;

  @BeforeEach
  void setUp() {
    certificateMessageTypeConverter = new CertificateMessageTypeConverter();
  }

  @Nested
  class QuestionTypeTests {

    @Test
    void shallConvertMissing() {
      final var certificateMessageType =
          CertificateMessageType.builder().type(MessageType.MISSING).subject(SUBJECT).build();

      assertEquals(
          QuestionTypeDTO.MISSING,
          certificateMessageTypeConverter.convert(certificateMessageType).getType());
    }

    @Test
    void shallConvertContact() {
      final var certificateMessageType =
          CertificateMessageType.builder().type(MessageType.CONTACT).subject(SUBJECT).build();

      assertEquals(
          QuestionTypeDTO.CONTACT,
          certificateMessageTypeConverter.convert(certificateMessageType).getType());
    }

    @Test
    void shallConvertOther() {
      final var certificateMessageType =
          CertificateMessageType.builder().type(MessageType.OTHER).subject(SUBJECT).build();

      assertEquals(
          QuestionTypeDTO.OTHER,
          certificateMessageTypeConverter.convert(certificateMessageType).getType());
    }

    @Test
    void shallConvertComplement() {
      final var certificateMessageType =
          CertificateMessageType.builder().type(MessageType.COMPLEMENT).subject(SUBJECT).build();

      assertEquals(
          QuestionTypeDTO.COMPLEMENT,
          certificateMessageTypeConverter.convert(certificateMessageType).getType());
    }
  }

  @Test
  void shallConvertSubject() {
    final var certificateMessageType =
        CertificateMessageType.builder().type(MessageType.COMPLEMENT).subject(SUBJECT).build();

    assertEquals(
        SUBJECT.subject(),
        certificateMessageTypeConverter.convert(certificateMessageType).getSubject());
  }
}
