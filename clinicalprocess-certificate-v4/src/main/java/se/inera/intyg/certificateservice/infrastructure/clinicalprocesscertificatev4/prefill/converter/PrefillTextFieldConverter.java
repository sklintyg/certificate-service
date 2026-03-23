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

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillAnswer;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillError;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.util.PrefillValidator;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.util.SubAnswersUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

@Component
public class PrefillTextFieldConverter implements PrefillStandardConverter {

  @Override
  public Class<? extends ElementConfiguration> supports() {
    return ElementConfigurationTextField.class;
  }

  @Override
  public PrefillAnswer prefillAnswer(ElementSpecification specification, Forifyllnad prefill) {
    if (!(specification.configuration()
        instanceof ElementConfigurationTextField configurationTextField)) {
      return PrefillAnswer.builder().errors(List.of(PrefillError.wrongConfigurationType())).build();
    }

    final var answers =
        prefill.getSvar().stream()
            .filter(svar -> svar.getId().equals(specification.id().id()))
            .toList();

    final var subAnswers =
        prefill.getSvar().stream()
            .map(Svar::getDelsvar)
            .flatMap(List::stream)
            .filter(delsvar -> delsvar.getId().equals(specification.id().id()))
            .toList();

    if (subAnswers.isEmpty() && answers.isEmpty()) {
      return null;
    }

    final var prefillErrors = new ArrayList<PrefillError>();
    prefillErrors.addAll(
        PrefillValidator.validateSingleAnswerOrSubAnswer(answers, subAnswers, specification));

    prefillErrors.addAll(
        PrefillValidator.validateDelsvarId(
            SubAnswersUtil.get(answers, subAnswers), configurationTextField, specification));

    if (!prefillErrors.isEmpty()) {
      return PrefillAnswer.builder().errors(prefillErrors).build();
    }

    return PrefillAnswer.builder()
        .elementData(
            ElementData.builder()
                .id(specification.id())
                .value(
                    ElementValueText.builder()
                        .textId(configurationTextField.id())
                        .text(
                            SubAnswersUtil.getContent(subAnswers, answers, configurationTextField))
                        .build())
                .build())
        .build();
  }
}
