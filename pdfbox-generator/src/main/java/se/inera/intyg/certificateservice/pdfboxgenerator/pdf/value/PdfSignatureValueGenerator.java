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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.TemplatePdfSpecification;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfSignatureValueGenerator {

  public List<PdfField> generate(Certificate certificate) {
    if (!(certificate.certificateModel().pdfSpecification()
        instanceof TemplatePdfSpecification templateSpec)) {
      throw new IllegalArgumentException(
          "PdfSignatureValueGenerator can only process TemplatePdfSpecification");
    }

    final var fields = new ArrayList<PdfField>();
    fields.add(getSignedDate(templateSpec, certificate));
    fields.add(getIssuerFullName(templateSpec, certificate));
    fields.add(getPaTitles(templateSpec, certificate).orElse(null));
    fields.add(getSpeciality(templateSpec, certificate).orElse(null));
    fields.add(getHsaId(templateSpec, certificate));
    fields.add(getWorkplaceCode(templateSpec, certificate).orElse(null));

    return fields;
  }

  private PdfField getSignedDate(TemplatePdfSpecification templateSpec, Certificate certificate) {
    return PdfField.builder()
        .id(templateSpec.signature().signedDateFieldId().id())
        .value(certificate.signed().format(DateTimeFormatter.ISO_DATE))
        .build();
  }

  private PdfField getIssuerFullName(
      TemplatePdfSpecification templateSpec, Certificate certificate) {
    return PdfField.builder()
        .id(templateSpec.signature().signedByNameFieldId().id())
        .value(certificate.getMetadataForPrint().issuer().name().fullName())
        .build();
  }

  private Optional<PdfField> getPaTitles(
      TemplatePdfSpecification templateSpec, Certificate certificate) {
    final var paTitles = certificate.getMetadataForPrint().issuer().paTitles();
    if (paTitles != null) {
      final var paTitleCodes =
          paTitles.stream().map(PaTitle::code).collect(Collectors.joining(", "));

      return Optional.of(
          PdfField.builder()
              .id(templateSpec.signature().paTitleFieldId().id())
              .value(paTitleCodes)
              .build());
    }

    return Optional.empty();
  }

  private Optional<PdfField> getSpeciality(
      TemplatePdfSpecification templateSpec, Certificate certificate) {
    final var specialities = certificate.getMetadataForPrint().issuer().specialities();
    if (specialities != null) {
      final var mappedSpecialities =
          specialities.stream().map(Speciality::value).collect(Collectors.joining(", "));

      return Optional.of(
          PdfField.builder()
              .id(templateSpec.signature().specialtyFieldId().id())
              .value(mappedSpecialities)
              .build());
    }

    return Optional.empty();
  }

  private PdfField getHsaId(TemplatePdfSpecification templateSpec, Certificate certificate) {
    final var hsaId = certificate.getMetadataForPrint().issuer().hsaId().id();
    return PdfField.builder().id(templateSpec.signature().hsaIdFieldId().id()).value(hsaId).build();
  }

  private Optional<PdfField> getWorkplaceCode(
      TemplatePdfSpecification templateSpec, Certificate certificate) {
    final var workplaceCode = certificate.getMetadataForPrint().issuingUnit().workplaceCode();
    if (workplaceCode != null) {
      return Optional.of(
          PdfField.builder()
              .id(templateSpec.signature().workplaceCodeFieldId().id())
              .value(workplaceCode.code())
              .build());
    }

    return Optional.empty();
  }
}
