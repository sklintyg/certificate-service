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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.CERTIFICATE_META_DATA;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_HSA_ID_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_PA_TITLE_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_SIGNED_BY_NAME_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_SIGNED_DATE_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_SPECIALTY_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_WORKPLACE_CODE_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_FIRST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_HSA_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_LAST_NAME;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.certificate.custom.provider.SignaturePdfFieldsProvider;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210TemplatePathProvider;

class SignaturePdfFieldsProviderTest {

  private static final LocalDateTime SIGNED_DATE = LocalDateTime.of(2025, 3, 15, 10, 0);
  private static final String EXPECTED_ISSUER_NAME =
      AJLA_DOCTOR_FIRST_NAME + " " + AJLA_DOCTOR_LAST_NAME;

  private final SignaturePdfFieldsProvider provider = new SignaturePdfFieldsProvider();
  private final CustomPdfSpecification spec =
      CustomPdfSpecification.builder()
          .pdfTemplatePathProvider(new FK7210TemplatePathProvider())
          .signature(
              CustomPdfSignature.builder()
                  .signedDateFieldId(FK7210_PDF_SIGNED_DATE_FIELD_ID)
                  .signedByNameFieldId(FK7210_PDF_SIGNED_BY_NAME_FIELD_ID)
                  .paTitleFieldId(FK7210_PDF_PA_TITLE_FIELD_ID)
                  .specialtyFieldId(FK7210_PDF_SPECIALTY_FIELD_ID)
                  .hsaIdFieldId(FK7210_PDF_HSA_ID_FIELD_ID)
                  .workplaceCodeFieldId(FK7210_PDF_WORKPLACE_CODE_FIELD_ID)
                  .contactInformation(
                      TestDataPdfSpecificationConstants.FK7210_PDF_CONTACT_INFORMATION)
                  .signaturePageIndex(0)
                  .build())
          .build();

  @Test
  void shallReturnEmptyMapWhenDraft() {
    final var certificate = fk7210CertificateBuilder().status(Status.DRAFT).signed(null).build();

    final var fields = provider.fields(certificate, spec);

    assertTrue(fields.isEmpty());
  }

  @Test
  void shallAddSignedDateField() {
    final var certificate = buildSignedCertificate();

    final var fields = provider.fields(certificate, spec);

    assertEquals(
        SIGNED_DATE.format(DateTimeFormatter.ISO_DATE),
        fields.get(FK7210_PDF_SIGNED_DATE_FIELD_ID.id()).value());
  }

  @Test
  void shallAddIssuerNameField() {
    final var certificate = buildSignedCertificate();

    final var fields = provider.fields(certificate, spec);

    assertEquals(EXPECTED_ISSUER_NAME, fields.get(FK7210_PDF_SIGNED_BY_NAME_FIELD_ID.id()).value());
  }

  @Test
  void shallAddHsaIdField() {
    final var certificate = buildSignedCertificate();

    final var fields = provider.fields(certificate, spec);

    assertEquals(AJLA_DOCTOR_HSA_ID, fields.get(FK7210_PDF_HSA_ID_FIELD_ID.id()).value());
  }

  @Test
  void shallAddPaTitleField() {
    final var certificate = buildSignedCertificate();

    final var fields = provider.fields(certificate, spec);

    assertEquals("203090, 601010", fields.get(FK7210_PDF_PA_TITLE_FIELD_ID.id()).value());
  }

  @Test
  void shallAddSpecialtyField() {
    final var certificate = buildSignedCertificate();

    final var fields = provider.fields(certificate, spec);

    assertEquals(
        "Allmänmedicin, Psykiatri", fields.get(FK7210_PDF_SPECIALTY_FIELD_ID.id()).value());
  }

  @Test
  void shallAddWorkplaceCodeField() {
    final var certificate = buildSignedCertificate();

    final var fields = provider.fields(certificate, spec);

    assertEquals("1627", fields.get(FK7210_PDF_WORKPLACE_CODE_FIELD_ID.id()).value());
  }

  private static se.inera.intyg.certificateservice.domain.certificate.model.Certificate
      buildSignedCertificate() {
    return fk7210CertificateBuilder()
        .status(Status.SIGNED)
        .signed(SIGNED_DATE)
        .metaDataFromSignInstance(CERTIFICATE_META_DATA)
        .build();
  }
}
