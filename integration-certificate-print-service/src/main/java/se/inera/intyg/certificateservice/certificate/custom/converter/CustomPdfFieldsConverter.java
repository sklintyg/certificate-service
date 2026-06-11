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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;

@Component
public class CustomPdfFieldsConverter {

  public Map<String, CustomPdfFieldDTO> convert(
      Certificate certificate, CustomPdfSpecification spec) {
    final var fields = new HashMap<String, CustomPdfFieldDTO>();

    addPatientFields(certificate, spec, fields);
    addDateElementFields(certificate, fields);

    if (certificate.status() == Status.SIGNED) {
      addSignatureFields(certificate, spec.signature(), fields);
    }

    addUnitContactInformation(certificate, spec.signature(), fields);

    return fields;
  }

  private static void addPatientFields(
      Certificate certificate, CustomPdfSpecification spec, Map<String, CustomPdfFieldDTO> fields) {
    final var patientId = certificate.getMetadataForPrint().patient().id().idWithoutDash();
    spec.patientIdFieldIds()
        .forEach(fieldId -> fields.put(fieldId.id(), new CustomPdfFieldDTO(patientId)));
  }

  private static void addDateElementFields(
      Certificate certificate, Map<String, CustomPdfFieldDTO> fields) {
    certificate.certificateModel().elementSpecifications().stream()
        .flatMap(ElementSpecification::flatten)
        .filter(es -> es.pdfConfiguration() instanceof PdfConfigurationDate)
        .forEach(
            elementSpec ->
                certificate
                    .getElementDataById(elementSpec.id())
                    .ifPresent(
                        data -> {
                          if (data.value() instanceof ElementValueDate valueDate
                              && valueDate.date() != null) {
                            final var config =
                                (PdfConfigurationDate) elementSpec.pdfConfiguration();
                            fields.put(
                                config.pdfFieldId().id(),
                                new CustomPdfFieldDTO(valueDate.date().toString()));
                          }
                        }));
  }

  private static void addSignatureFields(
      Certificate certificate, PdfSignature signature, Map<String, CustomPdfFieldDTO> fields) {
    final var metadata = certificate.getMetadataForPrint();

    fields.put(
        signature.signedDateFieldId().id(),
        new CustomPdfFieldDTO(certificate.signed().format(DateTimeFormatter.ISO_DATE)));
    fields.put(
        signature.signedByNameFieldId().id(),
        new CustomPdfFieldDTO(metadata.issuer().name().fullName()));

    final var paTitles = metadata.issuer().paTitles();
    if (paTitles != null) {
      fields.put(
          signature.paTitleFieldId().id(),
          new CustomPdfFieldDTO(
              paTitles.stream().map(PaTitle::code).collect(Collectors.joining(", "))));
    }

    final var specialities = metadata.issuer().specialities();
    if (specialities != null) {
      fields.put(
          signature.specialtyFieldId().id(),
          new CustomPdfFieldDTO(
              specialities.stream().map(Speciality::value).collect(Collectors.joining(", "))));
    }

    fields.put(
        signature.hsaIdFieldId().id(), new CustomPdfFieldDTO(metadata.issuer().hsaId().id()));

    final var workplaceCode = metadata.issuingUnit().workplaceCode();
    if (workplaceCode != null) {
      fields.put(
          signature.workplaceCodeFieldId().id(), new CustomPdfFieldDTO(workplaceCode.code()));
    }
  }

  private static void addUnitContactInformation(
      Certificate certificate, PdfSignature signature, Map<String, CustomPdfFieldDTO> fields) {
    final var unitName = certificate.getMetadataForPrint().issuingUnit().name().name();
    final var contactInfo =
        certificate
            .getElementDataById(UNIT_CONTACT_INFORMATION)
            .map(
                data -> {
                  if (data.value() instanceof ElementValueUnitContactInformation unitValue) {
                    return String.join(
                        "\n",
                        unitName,
                        String.join(
                            "",
                            unitValue.address(),
                            ", ",
                            String.join(" ", unitValue.zipCode(), unitValue.city())),
                        String.join(" ", "Telefon:", unitValue.phoneNumber()));
                  }
                  return "";
                })
            .orElse("");
    fields.put(signature.contactInformation().id(), new CustomPdfFieldDTO(contactInfo));
  }
}
