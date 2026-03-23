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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfig;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CheckboxMultipleCode;
import se.inera.intyg.certificateservice.application.certificate.dto.config.Layout;
import se.inera.intyg.certificateservice.application.certificate.dto.config.Message;
import se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

@Component
public class CertificateDataCheckboxMultipleCodeConfigConverter
    implements CertificateDataConfigConverter {

  @Override
  public ElementType getType() {
    return ElementType.CHECKBOX_MULTIPLE_CODE;
  }

  public CertificateDataConfig convert(
      ElementSpecification elementSpecification, Certificate certificate) {
    if (!(elementSpecification.configuration()
        instanceof ElementConfigurationCheckboxMultipleCode configuration)) {
      throw new IllegalStateException(
          "Invalid config type. Type was '%s'"
              .formatted(elementSpecification.configuration().type()));
    }
    return CertificateDataConfigCheckboxMultipleCode.builder()
        .text(configuration.name())
        .label(configuration.label())
        .description(configuration.description())
        .message(
            shouldIncludeMessage(certificate, configuration)
                ? Message.builder()
                    .content(configuration.message().content())
                    .level(MessageLevel.toMessageLevel(configuration.message().level()))
                    .build()
                : null)
        .layout(Layout.toLayout(configuration.elementLayout()))
        .list(
            configuration.list().stream()
                .map(
                    code ->
                        CheckboxMultipleCode.builder()
                            .id(code.id().value())
                            .label(code.label())
                            .build())
                .toList())
        .build();
  }

  private static boolean shouldIncludeMessage(
      Certificate certificate,
      ElementConfigurationCheckboxMultipleCode elementConfigurationCheckboxMultipleCode) {
    return elementConfigurationCheckboxMultipleCode.message() != null
        && elementConfigurationCheckboxMultipleCode.message().include(certificate);
  }
}
