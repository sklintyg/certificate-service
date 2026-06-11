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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;
import se.inera.intyg.certificateservice.domain.patient.model.Name;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.staff.model.Staff;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;
import se.inera.intyg.certificateservice.domain.unit.model.UnitName;
import se.inera.intyg.certificateservice.domain.unit.model.WorkplaceCode;

@ExtendWith(MockitoExtension.class)
class CustomPdfFieldsConverterTest {

  private static final String PATIENT_FIELD_ID = "form1[0].#subform[0].flt_txtPersonNr[0]";
  private static final String BIRTH_DATE_FIELD_ID = "form1[0].#subform[0].flt_dat[0]";
  private static final String SIGNED_DATE_FIELD_ID = "form1[0].#subform[0].flt_datUnderskrift[0]";
  private static final String SIGNED_BY_NAME_FIELD_ID =
      "form1[0].#subform[0].flt_txtNamnfortydligande[0]";
  private static final String PA_TITLE_FIELD_ID = "form1[0].#subform[0].flt_txtBefattning[0]";
  private static final String SPECIALTY_FIELD_ID =
      "form1[0].#subform[0].flt_txtEventuellSpecialistkompetens[0]";
  private static final String HSA_ID_FIELD_ID = "form1[0].#subform[0].flt_txtLakarensHSA-ID[0]";
  private static final String WORKPLACE_CODE_FIELD_ID =
      "form1[0].#subform[0].flt_txtArbetsplatskod[0]";
  private static final String CONTACT_INFO_FIELD_ID =
      "form1[0].#subform[0].flt_txtVardgivarensNamnAdressTelefon[0]";

  private static final String PATIENT_ID_WITHOUT_DASH = "194011306125";
  private static final String ISSUER_FULL_NAME = "Anna Lindqvist";
  private static final String ISSUER_HSA_ID = "SE2321000016-1C4B";
  private static final String PA_TITLE_CODE = "201010";
  private static final String SPECIALTY_VALUE = "Allmänmedicin";
  private static final String WORKPLACE_CODE_VALUE = "1627";
  private static final String UNIT_NAME = "Alfa Allergimottagningen";
  private static final String UNIT_ADDRESS = "Storgatan 1";
  private static final String UNIT_CITY = "Småmåla";
  private static final String UNIT_ZIP = "12345";
  private static final String UNIT_PHONE = "0101234567890";

  private static final LocalDate BIRTH_DATE = LocalDate.of(2025, 10, 20);
  private static final LocalDateTime SIGNED_AT = LocalDateTime.of(2025, 3, 15, 10, 0);
  private static final ElementId BIRTH_DATE_ELEMENT_ID = new ElementId("54");

  @InjectMocks private CustomPdfFieldsConverter converter;

  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;
  @Mock private CertificateMetaData metadata;
  @Mock private Staff issuer;
  @Mock private Name issuerName;
  @Mock private IssuingUnit issuingUnit;
  @Mock private UnitName issuingUnitName;
  @Mock private Patient patient;
  @Mock private PersonId patientPersonId;

  private CustomPdfSpecification spec;

  @BeforeEach
  void setUp() {
    spec =
        CustomPdfSpecification.builder()
            .pdfTemplatePath("fk7210/pdf/fk7210_v1.pdf")
            .pdfNoAddressTemplatePath("fk7210/pdf/fk7210_v1_no_address.pdf")
            .patientIdFieldIds(List.of(new PdfFieldId(PATIENT_FIELD_ID)))
            .signature(
                PdfSignature.builder()
                    .signedDateFieldId(new PdfFieldId(SIGNED_DATE_FIELD_ID))
                    .signedByNameFieldId(new PdfFieldId(SIGNED_BY_NAME_FIELD_ID))
                    .paTitleFieldId(new PdfFieldId(PA_TITLE_FIELD_ID))
                    .specialtyFieldId(new PdfFieldId(SPECIALTY_FIELD_ID))
                    .hsaIdFieldId(new PdfFieldId(HSA_ID_FIELD_ID))
                    .workplaceCodeFieldId(new PdfFieldId(WORKPLACE_CODE_FIELD_ID))
                    .contactInformation(new PdfFieldId(CONTACT_INFO_FIELD_ID))
                    .signatureWithAddressTagIndex(new PdfTagIndex(15))
                    .signatureWithoutAddressTagIndex(new PdfTagIndex(7))
                    .signaturePageIndex(0)
                    .build())
            .signatureTextX(100)
            .signatureTextY(50)
            .signatureTextFontSize(8)
            .build();

    when(certificate.certificateModel()).thenReturn(certificateModel);
    when(certificate.getMetadataForPrint()).thenReturn(metadata);
    when(metadata.patient()).thenReturn(patient);
    when(patient.id()).thenReturn(patientPersonId);
    when(patientPersonId.idWithoutDash()).thenReturn(PATIENT_ID_WITHOUT_DASH);
    when(metadata.issuingUnit()).thenReturn(issuingUnit);
    when(issuingUnit.name()).thenReturn(issuingUnitName);
    when(issuingUnitName.name()).thenReturn(UNIT_NAME);
    when(certificateModel.elementSpecifications()).thenReturn(List.of());
    when(certificate.getElementDataById(
            ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION))
        .thenReturn(Optional.empty());
  }

  @Nested
  class PatientFields {

    @Test
    void shallAddPatientIdField() {
      when(certificate.status()).thenReturn(Status.DRAFT);

      final var fields = converter.convert(certificate, spec);

      assertNotNull(fields.get(PATIENT_FIELD_ID));
      assertEquals(PATIENT_ID_WITHOUT_DASH, fields.get(PATIENT_FIELD_ID).value());
    }
  }

  @Nested
  class DateElementFields {

    @Test
    void shallAddDateFieldWhenElementValueDatePresent() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      when(certificateModel.elementSpecifications())
          .thenReturn(List.of(buildBirthDateElementSpec()));
      when(certificate.getElementDataById(BIRTH_DATE_ELEMENT_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(BIRTH_DATE_ELEMENT_ID)
                      .value(ElementValueDate.builder().date(BIRTH_DATE).build())
                      .build()));

      final var fields = converter.convert(certificate, spec);

      assertNotNull(fields.get(BIRTH_DATE_FIELD_ID));
      assertEquals(BIRTH_DATE.toString(), fields.get(BIRTH_DATE_FIELD_ID).value());
    }

    @Test
    void shallNotAddDateFieldWhenNoElementData() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      when(certificateModel.elementSpecifications())
          .thenReturn(List.of(buildBirthDateElementSpec()));
      when(certificate.getElementDataById(BIRTH_DATE_ELEMENT_ID)).thenReturn(Optional.empty());

      final var fields = converter.convert(certificate, spec);

      assertFalse(fields.containsKey(BIRTH_DATE_FIELD_ID));
    }
  }

  @Nested
  class SignatureFields {

    @BeforeEach
    void setUp() {
      when(certificate.status()).thenReturn(Status.SIGNED);
      when(certificate.signed()).thenReturn(SIGNED_AT);
      when(metadata.issuer()).thenReturn(issuer);
      when(issuer.name()).thenReturn(issuerName);
      when(issuerName.fullName()).thenReturn(ISSUER_FULL_NAME);
      when(issuer.paTitles()).thenReturn(List.of(new PaTitle(PA_TITLE_CODE, "Läkare")));
      when(issuer.specialities()).thenReturn(List.of(new Speciality(SPECIALTY_VALUE)));
      when(issuer.hsaId()).thenReturn(new HsaId(ISSUER_HSA_ID));
      when(issuingUnit.workplaceCode()).thenReturn(new WorkplaceCode(WORKPLACE_CODE_VALUE));
    }

    @Test
    void shallAddSignedDateField() {
      final var fields = converter.convert(certificate, spec);

      assertEquals("2025-03-15", fields.get(SIGNED_DATE_FIELD_ID).value());
    }

    @Test
    void shallAddIssuerNameField() {
      final var fields = converter.convert(certificate, spec);

      assertEquals(ISSUER_FULL_NAME, fields.get(SIGNED_BY_NAME_FIELD_ID).value());
    }

    @Test
    void shallAddPaTitleField() {
      final var fields = converter.convert(certificate, spec);

      assertEquals(PA_TITLE_CODE, fields.get(PA_TITLE_FIELD_ID).value());
    }

    @Test
    void shallAddSpecialtyField() {
      final var fields = converter.convert(certificate, spec);

      assertEquals(SPECIALTY_VALUE, fields.get(SPECIALTY_FIELD_ID).value());
    }

    @Test
    void shallAddHsaIdField() {
      final var fields = converter.convert(certificate, spec);

      assertEquals(ISSUER_HSA_ID, fields.get(HSA_ID_FIELD_ID).value());
    }

    @Test
    void shallAddWorkplaceCodeField() {
      final var fields = converter.convert(certificate, spec);

      assertEquals(WORKPLACE_CODE_VALUE, fields.get(WORKPLACE_CODE_FIELD_ID).value());
    }
  }

  @Test
  void shallNotAddSignatureFieldsWhenDraft() {
    when(certificate.status()).thenReturn(Status.DRAFT);

    final var fields = converter.convert(certificate, spec);

    assertFalse(fields.containsKey(SIGNED_DATE_FIELD_ID));
    assertFalse(fields.containsKey(SIGNED_BY_NAME_FIELD_ID));
    assertFalse(fields.containsKey(HSA_ID_FIELD_ID));
  }

  @Nested
  class UnitContactInformation {

    @Test
    void shallAddUnitContactInfoField() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      when(certificate.getElementDataById(
              ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION)
                      .value(
                          ElementValueUnitContactInformation.builder()
                              .address(UNIT_ADDRESS)
                              .city(UNIT_CITY)
                              .zipCode(UNIT_ZIP)
                              .phoneNumber(UNIT_PHONE)
                              .build())
                      .build()));

      final var fields = converter.convert(certificate, spec);

      final var expected =
          UNIT_NAME
              + "\n"
              + UNIT_ADDRESS
              + ", "
              + UNIT_ZIP
              + " "
              + UNIT_CITY
              + "\n"
              + "Telefon: "
              + UNIT_PHONE;
      assertEquals(expected, fields.get(CONTACT_INFO_FIELD_ID).value());
    }

    @Test
    void shallAddEmptyContactInfoWhenNoData() {
      when(certificate.status()).thenReturn(Status.DRAFT);

      final var fields = converter.convert(certificate, spec);

      assertEquals("", fields.get(CONTACT_INFO_FIELD_ID).value());
    }
  }

  private static ElementSpecification buildBirthDateElementSpec() {
    return ElementSpecification.builder()
        .id(BIRTH_DATE_ELEMENT_ID)
        .configuration(
            ElementConfigurationDate.builder().name("Datum").id(new FieldId("54.1")).build())
        .pdfConfiguration(
            PdfConfigurationDate.builder().pdfFieldId(new PdfFieldId(BIRTH_DATE_FIELD_ID)).build())
        .build();
  }
}
