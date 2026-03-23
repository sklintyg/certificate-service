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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.CERTIFICATE_META_DATA;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.text.PdfTextGenerator;

@ExtendWith(MockitoExtension.class)
class PdfSignatureValueGeneratorTest {

  private static final LocalDateTime SIGNED = LocalDateTime.now();

  @Mock PdfTextGenerator pdfTextGenerator;

  @InjectMocks PdfSignatureValueGenerator pdfSignatureValueGenerator;

  @Test
  void shouldAddSignedDate() {
    final var certificate = getCertificate();
    final var expected =
        PdfField.builder()
            .id("form1[0].#subform[0].flt_datUnderskrift[0]")
            .value(SIGNED.format(DateTimeFormatter.ISO_DATE))
            .build();

    final var result = pdfSignatureValueGenerator.generate(certificate);

    assertTrue(result.contains(expected), "Expected signed date to be included in result");
  }

  @Test
  void shouldAddIssuerName() {
    final var certificate = getCertificate();
    final var expected =
        PdfField.builder()
            .id("form1[0].#subform[0].flt_txtNamnfortydligande[0]")
            .value(certificate.getMetadataForPrint().issuer().name().fullName())
            .build();

    final var result = pdfSignatureValueGenerator.generate(certificate);
    assertTrue(result.contains(expected), "Expected signature to be included in result");
  }

  @Test
  void shouldAddIssuerHsaId() {
    final var certificate = getCertificate();
    final var expected =
        PdfField.builder()
            .id("form1[0].#subform[0].flt_txtLakarensHSA-ID[0]")
            .value(certificate.getMetadataForPrint().issuer().hsaId().id())
            .build();

    final var result = pdfSignatureValueGenerator.generate(certificate);
    assertTrue(result.contains(expected), "Expected signature to be included in result");
  }

  @Test
  void shouldSetPaTitles() {
    final var certificate = getCertificate();
    final var expected =
        PdfField.builder()
            .id("form1[0].#subform[0].flt_txtBefattning[0]")
            .value("203090, 601010")
            .build();

    final var result = pdfSignatureValueGenerator.generate(certificate);
    assertTrue(result.contains(expected), "Expected PA-titles to be included in result");
  }

  @Test
  void shouldSetSpeciality() {
    final var certificate = getCertificate();
    final var expected =
        PdfField.builder()
            .id("form1[0].#subform[0].flt_txtEventuellSpecialistkompetens[0]")
            .value("Allmänmedicin, Psykiatri")
            .build();

    final var result = pdfSignatureValueGenerator.generate(certificate);
    assertTrue(result.contains(expected), "Expected speciality to be included in result");
  }

  @Test
  void shouldSetWorkplaceCode() {
    final var certificate = getCertificate();
    final var expected =
        PdfField.builder()
            .id("form1[0].#subform[0].flt_txtArbetsplatskod[0]")
            .value("1627")
            .build();

    final var result = pdfSignatureValueGenerator.generate(certificate);
    assertTrue(result.contains(expected), "Expected speciality to be included in result");
  }

  private static Certificate getCertificate() {
    return fk7210CertificateBuilder()
        .signed(SIGNED)
        .certificateMetaData(null)
        .metaDataFromSignInstance(CERTIFICATE_META_DATA)
        .build();
  }
}
