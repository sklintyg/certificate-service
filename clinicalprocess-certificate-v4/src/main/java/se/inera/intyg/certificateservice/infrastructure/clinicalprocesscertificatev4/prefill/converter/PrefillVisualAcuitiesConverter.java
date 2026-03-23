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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.Correction;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificate.model.VisualAcuity;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisualAcuity;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillAnswer;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillError;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.util.PrefillValidator;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

@Component
public class PrefillVisualAcuitiesConverter implements PrefillStandardConverter {

  @Override
  public Class<? extends ElementConfiguration> supports() {
    return ElementConfigurationVisualAcuities.class;
  }

  @Override
  public PrefillAnswer prefillAnswer(ElementSpecification specification, Forifyllnad prefill) {
    if (!(specification.configuration()
        instanceof ElementConfigurationVisualAcuities configurationVisualAcuities)) {
      return PrefillAnswer.builder().errors(List.of(PrefillError.wrongConfigurationType())).build();
    }

    final var answers =
        prefill.getSvar().stream()
            .filter(svar -> svar.getId().equals(specification.id().id()))
            .toList();

    if (answers.isEmpty()) {
      return null;
    }

    final var prefillError =
        PrefillValidator.validateMinimumNumberOfDelsvar(answers, 1, specification);

    if (!prefillError.isEmpty()) {
      return PrefillAnswer.builder().errors(prefillError).build();
    }

    if (answers.size() != 1) {
      return PrefillAnswer.builder()
          .errors(
              List.of(
                  PrefillError.wrongNumberOfAnswers(specification.id().id(), 1, answers.size())))
          .build();
    }

    final var answer = answers.stream().findFirst().orElseThrow();

    try {
      return PrefillAnswer.builder()
          .elementData(
              ElementData.builder()
                  .id(specification.id())
                  .value(
                      ElementValueVisualAcuities.builder()
                          .rightEye(
                              buildVisualAcuity(configurationVisualAcuities.rightEye(), answer))
                          .leftEye(buildVisualAcuity(configurationVisualAcuities.leftEye(), answer))
                          .binocular(
                              buildVisualAcuity(configurationVisualAcuities.binocular(), answer))
                          .build())
                  .build())
          .build();
    } catch (Exception ex) {
      return PrefillAnswer.invalidFormat(specification.id().id(), ex.getMessage());
    }
  }

  private VisualAcuity buildVisualAcuity(ElementVisualAcuity elementVisualAcuity, Svar answer) {
    final var subAnswerWithCorrectionId =
        answer.getDelsvar().stream()
            .filter(a -> a.getId().equals(elementVisualAcuity.withCorrectionId()))
            .findFirst()
            .map(Delsvar::getContent)
            .map(List::getFirst)
            .stream()
            .findFirst();

    final var subAnswerWithoutCorrectionId =
        answer.getDelsvar().stream()
            .filter(a -> a.getId().equals(elementVisualAcuity.withoutCorrectionId()))
            .findFirst()
            .map(Delsvar::getContent)
            .map(List::getFirst)
            .stream()
            .findFirst();

    if (subAnswerWithoutCorrectionId.isEmpty() && subAnswerWithCorrectionId.isEmpty()) {
      return null;
    }

    return VisualAcuity.builder()
        .withCorrection(
            subAnswerWithCorrectionId
                .map(
                    value ->
                        Correction.builder()
                            .id(new FieldId(elementVisualAcuity.withCorrectionId()))
                            .value(Double.parseDouble(((String) value).replace(",", ".")))
                            .build())
                .orElse(null))
        .withoutCorrection(
            subAnswerWithoutCorrectionId
                .map(
                    value ->
                        Correction.builder()
                            .id(new FieldId(elementVisualAcuity.withoutCorrectionId()))
                            .value(Double.parseDouble(((String) value).replace(",", ".")))
                            .build())
                .orElse(null))
        .build();
  }
}
