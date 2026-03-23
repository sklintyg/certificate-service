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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.util;

import java.util.List;
import java.util.stream.Stream;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public class SubAnswersUtil {

  private SubAnswersUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<Delsvar> get(List<Svar> answers, List<Delsvar> subAnswers) {
    return Stream.concat(
            answers.stream().map(Svar::getDelsvar).flatMap(List::stream), subAnswers.stream())
        .toList();
  }

  public static String getContent(
      List<Delsvar> subAnswers, List<Svar> answers, ElementConfiguration configuration) {
    if (!subAnswers.isEmpty()) {
      return (String)
          subAnswers.stream()
              .filter(subAnswer -> subAnswer.getId().equals(configuration.id().value()))
              .toList()
              .getFirst()
              .getContent()
              .getFirst();
    }
    return (String)
        answers.getFirst().getDelsvar().stream()
            .filter(subAnswer -> subAnswer.getId().equals(configuration.id().value()))
            .toList()
            .getFirst()
            .getContent()
            .getFirst();
  }
}
