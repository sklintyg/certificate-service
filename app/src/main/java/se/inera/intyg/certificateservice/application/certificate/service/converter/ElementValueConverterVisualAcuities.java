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

import static se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType.VISUAL_ACUITIES;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValue;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDouble;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificate.model.Correction;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificate.model.VisualAcuity;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Component
@RequiredArgsConstructor
public class ElementValueConverterVisualAcuities implements ElementValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return VISUAL_ACUITIES;
  }

  @Override
  public ElementValue convert(CertificateDataValue value) {
    if (!(value instanceof CertificateDataValueVisualAcuities visualAcuities)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(value.getType()));
    }

    return ElementValueVisualAcuities.builder()
        .rightEye(
            VisualAcuity.builder()
                .withCorrection(
                    Correction.builder()
                        .id(new FieldId(visualAcuities.getRightEye().getWithCorrection().getId()))
                        .value(getIfPresent(visualAcuities.getRightEye().getWithCorrection()))
                        .build())
                .withoutCorrection(
                    Correction.builder()
                        .id(
                            new FieldId(
                                visualAcuities.getRightEye().getWithoutCorrection().getId()))
                        .value(getIfPresent(visualAcuities.getRightEye().getWithoutCorrection()))
                        .build())
                .build())
        .leftEye(
            VisualAcuity.builder()
                .withCorrection(
                    Correction.builder()
                        .id(new FieldId(visualAcuities.getLeftEye().getWithCorrection().getId()))
                        .value(getIfPresent(visualAcuities.getLeftEye().getWithCorrection()))
                        .build())
                .withoutCorrection(
                    Correction.builder()
                        .id(new FieldId(visualAcuities.getLeftEye().getWithoutCorrection().getId()))
                        .value(getIfPresent(visualAcuities.getLeftEye().getWithoutCorrection()))
                        .build())
                .build())
        .binocular(
            VisualAcuity.builder()
                .withCorrection(
                    Correction.builder()
                        .id(new FieldId(visualAcuities.getBinocular().getWithCorrection().getId()))
                        .value(getIfPresent(visualAcuities.getBinocular().getWithCorrection()))
                        .build())
                .withoutCorrection(
                    Correction.builder()
                        .id(
                            new FieldId(
                                visualAcuities.getBinocular().getWithoutCorrection().getId()))
                        .value(getIfPresent(visualAcuities.getBinocular().getWithoutCorrection()))
                        .build())
                .build())
        .build();
  }

  private static Double getIfPresent(CertificateDataValueDouble certificateDataValueDouble) {
    return certificateDataValueDouble != null ? certificateDataValueDouble.getValue() : null;
  }
}
