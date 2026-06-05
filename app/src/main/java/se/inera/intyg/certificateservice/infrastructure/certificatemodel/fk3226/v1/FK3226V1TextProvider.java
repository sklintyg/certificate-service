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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1;

import se.inera.intyg.certificateservice.domain.common.model.Code;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateTextProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0009;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.FK3226TextKey;

public class FK3226V1TextProvider implements CertificateTextProvider {

  @Override
  public String text(Enum<?> key) {
    return switch ((FK3226TextKey) key) {
      case CATEGORY_HOT_DESCRIPTION ->
          """
          Ange på vilket sätt hälsotillståndet utgör ett påtagligt hot mot patientens liv i nuläget eller på viss tids sikt.

          Hälsotillståndet kan utgöra ett påtagligt hot även om det finns hopp om att det förbättras.
          <ul>
          <li>Ange alla diagnoser som sammantaget medför ett påtagligt hot mot patientens liv.</li><li>Ange ett av alternativen som gäller patientens behandling och vårdsituation.</li></ul>""";
      case QUESTION_PATIENTENS_VARDSITUATION_NAME -> "Patientens behandling och vårdsituation";
      case QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_NAME ->
          "Ange när den aktiva behandlingen avslutades";
      case CODE_ENDAST_PALLIATIV -> CodeSystemKvFkmu0009.ENDAST_PALLIATIV_V1.displayName();
    };
  }

  @Override
  public Code code(Enum<?> key) {
    return switch ((FK3226TextKey) key) {
      case CODE_ENDAST_PALLIATIV -> CodeSystemKvFkmu0009.ENDAST_PALLIATIV_V1;
      default -> throw new IllegalArgumentException("No code mapping for key: " + key);
    };
  }
}
