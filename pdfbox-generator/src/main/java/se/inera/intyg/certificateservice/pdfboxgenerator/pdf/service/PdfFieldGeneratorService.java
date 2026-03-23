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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.CertificatePdfContext;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value.PdfElementValueGenerator;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value.PdfPatientValueGenerator;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value.PdfSignatureValueGenerator;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value.PdfUnitValueGenerator;

@RequiredArgsConstructor
@Service
public class PdfFieldGeneratorService {

  private final PdfUnitValueGenerator pdfUnitValueGenerator;
  private final PdfPatientValueGenerator pdfPatientValueGenerator;
  private final PdfSignatureValueGenerator pdfSignatureValueGenerator;
  private final PdfElementValueGenerator pdfElementValueGenerator;

  public List<PdfField> generatePdfFields(CertificatePdfContext context) {
    final var certificate = context.getCertificate();

    return Stream.of(
            pdfUnitValueGenerator.generate(certificate),
            pdfPatientValueGenerator.generate(
                context.getCertificate(),
                context.getTemplatePdfSpecification().patientIdFieldIds()),
            getSignatureFields(certificate),
            pdfElementValueGenerator.generate(certificate))
        .flatMap(Collection::stream)
        .toList();
  }

  private List<PdfField> getSignatureFields(Certificate certificate) {
    return certificate.status() == Status.SIGNED
        ? pdfSignatureValueGenerator.generate(certificate)
        : List.of();
  }
}
