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
package se.inera.intyg.certificateservice.certificate.custom.provider;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;

@Component
public class SignaturePdfFieldsProvider implements PdfFieldsProvider {

  @Override
  public Map<String, CustomPdfFieldDTO> fields(
      Certificate certificate, CustomPdfSpecification spec) {
    if (certificate.status() != Status.SIGNED) {
      return Map.of();
    }
    final var metadata = certificate.getMetadataForPrint();
    return Stream.concat(
            requiredFields(certificate, spec.signature(), metadata).entrySet().stream(),
            optionalFields(spec.signature(), metadata).entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, CustomPdfFieldDTO> requiredFields(
      Certificate certificate, CustomPdfSignature signature, CertificateMetaData metadata) {
    return Map.of(
        signature.signedDateFieldId().id(),
        new CustomPdfFieldDTO(certificate.signed().format(DateTimeFormatter.ISO_DATE)),
        signature.signedByNameFieldId().id(),
        new CustomPdfFieldDTO(metadata.issuer().name().fullName()),
        signature.hsaIdFieldId().id(),
        new CustomPdfFieldDTO(metadata.issuer().hsaId().id()));
  }

  private static Map<String, CustomPdfFieldDTO> optionalFields(
      CustomPdfSignature signature, CertificateMetaData metadata) {
    return Stream.of(
            Optional.ofNullable(metadata.issuer().paTitles())
                .map(
                    titles ->
                        Map.entry(
                            signature.paTitleFieldId().id(),
                            new CustomPdfFieldDTO(
                                titles.stream()
                                    .map(PaTitle::code)
                                    .collect(Collectors.joining(", "))))),
            Optional.ofNullable(metadata.issuer().specialities())
                .map(
                    specs ->
                        Map.entry(
                            signature.specialtyFieldId().id(),
                            new CustomPdfFieldDTO(
                                specs.stream()
                                    .map(Speciality::value)
                                    .collect(Collectors.joining(", "))))),
            Optional.ofNullable(metadata.issuingUnit().workplaceCode())
                .map(
                    wc ->
                        Map.entry(
                            signature.workplaceCodeFieldId().id(),
                            new CustomPdfFieldDTO(wc.code()))))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
