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

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;

@Component
public class CustomPdfFieldsConverter {

  public Map<String, CustomPdfFieldDTO> convert(
      Certificate certificate, CustomPdfSpecification spec) {
    return Stream.of(
            patientFields(certificate, spec),
            dateElementFields(certificate),
            signatureFields(certificate, spec),
            unitContactInformation(certificate, spec.signature()))
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, CustomPdfFieldDTO> patientFields(
      Certificate certificate, CustomPdfSpecification spec) {
    final var patientId = certificate.getMetadataForPrint().patient().id().idWithoutDash();
    return spec.patientIdFieldIds().stream()
        .collect(Collectors.toMap(PdfFieldId::id, fieldId -> new CustomPdfFieldDTO(patientId)));
  }

  private static Map<String, CustomPdfFieldDTO> dateElementFields(Certificate certificate) {
    return certificate.certificateModel().elementSpecifications().stream()
        .flatMap(ElementSpecification::flatten)
        .filter(es -> es.pdfConfiguration() instanceof PdfConfigurationDate)
        .flatMap(
            elementSpec ->
                certificate
                    .getElementDataById(elementSpec.id())
                    .filter(data -> data.value() instanceof ElementValueDate v && v.date() != null)
                    .map(
                        data -> {
                          final var config = (PdfConfigurationDate) elementSpec.pdfConfiguration();
                          final var date = ((ElementValueDate) data.value()).date();
                          return Map.entry(
                              config.pdfFieldId().id(), new CustomPdfFieldDTO(date.toString()));
                        })
                    .stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, CustomPdfFieldDTO> signatureFields(
      Certificate certificate, CustomPdfSpecification spec) {
    return certificate.status() == Status.SIGNED
        ? signatureFields(certificate, spec.signature())
        : Map.of();
  }

  private static Map<String, CustomPdfFieldDTO> signatureFields(
      Certificate certificate, PdfSignature signature) {
    final var metadata = certificate.getMetadataForPrint();
    return Stream.concat(
            requiredSignatureFields(certificate, signature, metadata).entrySet().stream(),
            optionalSignatureFields(signature, metadata).entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, CustomPdfFieldDTO> requiredSignatureFields(
      Certificate certificate, PdfSignature signature, CertificateMetaData metadata) {
    return Map.of(
        signature.signedDateFieldId().id(),
        new CustomPdfFieldDTO(certificate.signed().format(DateTimeFormatter.ISO_DATE)),
        signature.signedByNameFieldId().id(),
        new CustomPdfFieldDTO(metadata.issuer().name().fullName()),
        signature.hsaIdFieldId().id(),
        new CustomPdfFieldDTO(metadata.issuer().hsaId().id()));
  }

  private static Map<String, CustomPdfFieldDTO> optionalSignatureFields(
      PdfSignature signature, CertificateMetaData metadata) {
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

  private static Map<String, CustomPdfFieldDTO> unitContactInformation(
      Certificate certificate, PdfSignature signature) {
    final var unitName = certificate.getMetadataForPrint().issuingUnit().name().name();
    final var contactInfo =
        certificate
            .getElementDataById(UNIT_CONTACT_INFORMATION)
            .filter(data -> data.value() instanceof ElementValueUnitContactInformation)
            .map(data -> buildAddress(unitName, (ElementValueUnitContactInformation) data.value()))
            .orElse("");
    return Map.of(signature.contactInformation().id(), new CustomPdfFieldDTO(contactInfo));
  }

  private static String buildAddress(
      String unitName, ElementValueUnitContactInformation unitValue) {
    return String.join(
        "\n",
        unitName,
        String.join(
            "", unitValue.address(), ", ", String.join(" ", unitValue.zipCode(), unitValue.city())),
        String.join(" ", "Telefon:", unitValue.phoneNumber()));
  }
}
