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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;

@Data
@Builder
public class PrefillAnswer {

  @JsonIgnore private ElementData elementData;
  @Default private List<PrefillError> errors = new ArrayList<>();

  public static PrefillAnswer answerNotFound(String id) {
    return PrefillAnswer.builder().errors(List.of(PrefillError.answerNotFound(id))).build();
  }

  public static PrefillAnswer invalidFormat(String answerId, String exceptionMessage) {
    return PrefillAnswer.builder()
        .errors(List.of(PrefillError.invalidFormat(answerId, exceptionMessage)))
        .build();
  }
}
