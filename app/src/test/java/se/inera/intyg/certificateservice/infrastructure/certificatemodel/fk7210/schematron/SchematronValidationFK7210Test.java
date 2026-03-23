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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.schematron;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionFactory;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.CertificateModelFactoryFK7210;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate.XmlGeneratorCertificateV4;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate.XmlGeneratorDate;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate.XmlGeneratorDateRangeList;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate.XmlGeneratorText;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate.XmlGeneratorValue;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.validation.SchemaValidatorV4;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.validation.SchematronValidator;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.validation.XmlValidationService;

@ExtendWith(MockitoExtension.class)
class SchematronValidationFK7210Test {

  @Mock private CertificateActionFactory certificateActionFactory;
  private SchematronValidator schematronValidator;
  private final XmlGeneratorCertificateV4 generator =
      new XmlGeneratorCertificateV4(
          new XmlGeneratorValue(
              List.of(
                  new XmlGeneratorDate(), new XmlGeneratorDateRangeList(), new XmlGeneratorText()),
              Collections.emptyList()),
          new XmlValidationService(new SchematronValidator(), new SchemaValidatorV4()));
  private CertificateModelFactoryFK7210 certificateModelFactoryFK7210;

  @BeforeEach
  void setUp() {
    schematronValidator = new SchematronValidator();
    certificateModelFactoryFK7210 = new CertificateModelFactoryFK7210(certificateActionFactory);
  }

  @Test
  void shallReturnTrueForValidCertificate() {
    final var element =
        ElementData.builder()
            .id(new ElementId("54"))
            .value(
                ElementValueDate.builder()
                    .dateId(new FieldId("54.1"))
                    .date(LocalDate.now())
                    .build())
            .build();

    final var certificate =
        TestDataCertificate.fk7210CertificateBuilder()
            .certificateModel(certificateModelFactoryFK7210.create())
            .elementData(List.of(element))
            .build();

    final var xml = generator.generate(certificate, true);

    assertTrue(
        schematronValidator.validate(
            certificate.id(), xml, CertificateModelFactoryFK7210.SCHEMATRON_PATH));
  }

  @Nested
  class QuestionFodelsedatum {

    @Test
    void shallReturnFalseIfValueIsNull() {
      final var element =
          ElementData.builder()
              .id(new ElementId("54"))
              .value(ElementValueDate.builder().dateId(new FieldId("54.1")).build())
              .build();

      final var certificate =
          TestDataCertificate.fk7210CertificateBuilder()
              .certificateModel(certificateModelFactoryFK7210.create())
              .elementData(List.of(element))
              .build();

      assertThrows(IllegalStateException.class, () -> generator.generate(certificate, true));
    }

    @Test
    void shallReturnFalseIfValueIsBeforeToday() {
      final var element =
          ElementData.builder()
              .id(new ElementId("54"))
              .value(
                  ElementValueDate.builder()
                      .dateId(new FieldId("54.1"))
                      .date(LocalDate.now().minusDays(1))
                      .build())
              .build();

      final var certificate =
          TestDataCertificate.fk7210CertificateBuilder()
              .certificateModel(certificateModelFactoryFK7210.create())
              .elementData(List.of(element))
              .build();

      assertThrows(IllegalStateException.class, () -> generator.generate(certificate, true));
    }

    @Test
    void shallReturnFalseIfValueIsMoreThanOneYearInTheFuture() {
      final var element =
          ElementData.builder()
              .id(new ElementId("54"))
              .value(
                  ElementValueDate.builder()
                      .dateId(new FieldId("54.1"))
                      .date(LocalDate.now().plusYears(1).plusDays(2))
                      .build())
              .build();

      final var certificate =
          TestDataCertificate.fk7210CertificateBuilder()
              .certificateModel(certificateModelFactoryFK7210.create())
              .elementData(List.of(element))
              .build();

      assertThrows(IllegalStateException.class, () -> generator.generate(certificate, true));
    }

    @Test
    void shallReturnFalseIfQuestionMissing() {
      final var element =
          ElementData.builder()
              .id(new ElementId("54"))
              .value(ElementValueDate.builder().build())
              .build();

      final var certificate =
          TestDataCertificate.fk7210CertificateBuilder()
              .certificateModel(certificateModelFactoryFK7210.create())
              .elementData(List.of(element))
              .build();

      assertThrows(IllegalStateException.class, () -> generator.generate(certificate, true));
    }
  }
}
