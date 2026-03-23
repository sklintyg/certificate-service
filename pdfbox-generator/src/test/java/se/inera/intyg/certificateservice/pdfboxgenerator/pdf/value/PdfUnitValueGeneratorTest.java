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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.CERTIFICATE_META_DATA;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataElementData.contactInfoElementDataBuilder;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@ExtendWith(MockitoExtension.class)
class PdfUnitValueGeneratorTest {

  @InjectMocks PdfUnitValueGenerator pdfUnitValueGenerator;

  @Test
  void shouldSetContactInformationOfUnitToField() {
    final var certificate = buildCertificate();
    final var elementData =
        certificate.getElementDataById(new ElementId("UNIT_CONTACT_INFORMATION"));
    final var unitValue = (ElementValueUnitContactInformation) elementData.get().value();
    final var expectedAddress =
        CERTIFICATE_META_DATA.issuingUnit().name().name()
            + "\n"
            + unitValue.address()
            + ", "
            + unitValue.zipCode()
            + " "
            + unitValue.city()
            + "\nTelefon: "
            + unitValue.phoneNumber();

    final var expected =
        List.of(
            PdfField.builder()
                .id("form1[0].#subform[0].flt_txtVardgivarensNamnAdressTelefon[0]")
                .value(expectedAddress)
                .build());

    final var result = pdfUnitValueGenerator.generate(certificate);

    assertEquals(expected.get(0).getValue(), result.get(0).getValue());
  }

  private Certificate buildCertificate() {
    return fk7210CertificateBuilder()
        .certificateMetaData(null)
        .metaDataFromSignInstance(CERTIFICATE_META_DATA)
        .elementData(List.of(contactInfoElementDataBuilder().build()))
        .build();
  }
}
