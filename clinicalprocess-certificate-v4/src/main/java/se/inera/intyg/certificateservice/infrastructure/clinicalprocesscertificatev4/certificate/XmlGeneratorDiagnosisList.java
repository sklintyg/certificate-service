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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosisList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDiagnosis;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

@Component
public class XmlGeneratorDiagnosisList implements XmlGeneratorElementValue {

  @Override
  public Class<? extends ElementValue> supports() {
    return ElementValueDiagnosisList.class;
  }

  public List<Svar> generate(ElementData data, ElementSpecification specification) {
    if (!(data.value() instanceof ElementValueDiagnosisList diagnosisList)) {
      return Collections.emptyList();
    }

    if (!(specification.configuration()
        instanceof ElementConfigurationDiagnosis configurationDiagnosis)) {
      throw new IllegalArgumentException(
          "Cannot generate xml for configuration of type '%s'"
              .formatted(specification.configuration().getClass()));
    }

    if (diagnosisList.diagnoses() == null || diagnosisList.diagnoses().isEmpty()) {
      return Collections.emptyList();
    }

    final var objectFactory = new ObjectFactory();
    final var atomicInteger = new AtomicInteger(1);
    return diagnosisList.diagnoses().stream()
        .map(
            diagnosis -> {
              final var diagnosisAnswer = new Svar();
              diagnosisAnswer.setId(data.id().id());
              diagnosisAnswer.setInstans(atomicInteger.getAndIncrement());

              final var subAnswerDiagnosisDescription = new Delsvar();
              subAnswerDiagnosisDescription.setId(getIdDescription(data));
              subAnswerDiagnosisDescription.getContent().add(diagnosis.description());

              final var subAnswerDiagnosisCode = new Delsvar();
              subAnswerDiagnosisCode.setId(getIdCode(data));

              final var cvType = new CVType();
              cvType.setCode(diagnosis.code());
              cvType.setDisplayName(diagnosis.description());
              cvType.setCodeSystem(configurationDiagnosis.codeSystem(diagnosis.terminology()));

              final var convertedCvType = objectFactory.createCv(cvType);
              subAnswerDiagnosisCode.getContent().add(convertedCvType);

              diagnosisAnswer.getDelsvar().add(subAnswerDiagnosisCode);
              diagnosisAnswer.getDelsvar().add(subAnswerDiagnosisDescription);
              return diagnosisAnswer;
            })
        .toList();
  }

  private static String getIdDescription(ElementData data) {
    return data.id().id() + ".1";
  }

  private static String getIdCode(ElementData data) {
    return data.id().id() + ".2";
  }
}
