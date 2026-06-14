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
package se.inera.intyg.certificateservice.certificate.custom.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7210certificateModelBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_FODELSEDATUM_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_QUESTION_BERAKNAT_FODELSEDATUM_ID;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.certificate.custom.provider.ElementPdfFieldsProvider;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210TemplatePathProvider;

class ElementPdfFieldsProviderTest {

  private static final LocalDate BIRTH_DATE = LocalDate.of(2025, 10, 20);

  private final ElementPdfFieldsProvider provider = new ElementPdfFieldsProvider();
  private final CustomPdfSpecification spec =
      CustomPdfSpecification.builder()
          .pdfTemplatePathProvider(new FK7210TemplatePathProvider())
          .build();

  @Nested
  class ElementValueDate {
    @Test
    void shallAddDateFieldWhenElementValueDatePresent() {
      final var certificate =
          fk7210CertificateBuilder()
              .certificateModel(
                  fk7210certificateModelBuilder()
                      .elementSpecifications(List.of(birthDateElementSpec()))
                      .build())
              .elementData(
                  List.of(
                      ElementData.builder()
                          .id(FK7210_QUESTION_BERAKNAT_FODELSEDATUM_ID)
                          .value(
                              se.inera.intyg.certificateservice.domain.certificate.model
                                  .ElementValueDate.builder()
                                  .date(BIRTH_DATE)
                                  .build())
                          .build()))
              .build();

      final var fields = provider.fields(certificate, spec);

      assertEquals(
          BIRTH_DATE.toString(), fields.get(FK7210_PDF_FODELSEDATUM_FIELD_ID.id()).value());
    }

    @Test
    void shallNotAddDateFieldWhenNoElementData() {
      final var certificate =
          fk7210CertificateBuilder()
              .certificateModel(
                  fk7210certificateModelBuilder()
                      .elementSpecifications(List.of(birthDateElementSpec()))
                      .build())
              .elementData(List.of())
              .build();

      final var fields = provider.fields(certificate, spec);

      assertFalse(fields.containsKey(FK7210_PDF_FODELSEDATUM_FIELD_ID.id()));
    }

    private static ElementSpecification birthDateElementSpec() {
      return ElementSpecification.builder()
          .id(FK7210_QUESTION_BERAKNAT_FODELSEDATUM_ID)
          .configuration(
              ElementConfigurationDate.builder().name("Datum").id(new FieldId("54.1")).build())
          .pdfConfiguration(
              PdfConfigurationDate.builder().pdfFieldId(FK7210_PDF_FODELSEDATUM_FIELD_ID).build())
          .build();
    }
  }
}
