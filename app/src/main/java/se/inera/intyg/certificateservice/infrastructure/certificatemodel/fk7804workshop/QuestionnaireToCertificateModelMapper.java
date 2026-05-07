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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804workshop;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r5.model.Questionnaire;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateTypeName;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateVersion;

/**
 * Maps a FHIR R5 Questionnaire to a CertificateModel, including metadata and element
 * specifications.
 */
@Slf4j
public class QuestionnaireToCertificateModelMapper {

  private QuestionnaireToCertificateModelMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Maps a FHIR Questionnaire to a CertificateModel.
   *
   * <p>Extracts metadata (id, name, version, description) from the Questionnaire resource
   * and delegates element mapping to {@link QuestionnaireToElementSpecificationMapper}.
   *
   * @param questionnaire the FHIR Questionnaire resource
   * @return a CertificateModel representing the questionnaire
   */
  public static CertificateModel map(Questionnaire questionnaire) {
    if (questionnaire == null) {
      throw new IllegalArgumentException("Questionnaire cannot be null!");
    }

    final var type = resolveType(questionnaire);
    final var version = resolveVersion(questionnaire);
    final var name = resolveName(questionnaire);
    final var description = resolveDescription(questionnaire);
    final var typeName = resolveTypeName(questionnaire);
    final var elementSpecifications =
        QuestionnaireToElementSpecificationMapper.map(questionnaire);

    return CertificateModel.builder()
        .id(
            CertificateModelId.builder()
                .type(new CertificateType(type))
                .version(new CertificateVersion(version))
                .build()
        )
        .typeName(new CertificateTypeName(typeName))
        .name(name)
        .description(description)
        .detailedDescription(resolvePurpose(questionnaire))
        .activeFrom(LocalDateTime.now(ZoneId.systemDefault()))
        .availableForCitizen(true)
        .elementSpecifications(elementSpecifications)
        .certificateActionSpecifications(Collections.emptyList())
        .messageActionSpecifications(Collections.emptyList())
        .texts(Collections.emptyList())
        .messageTypes(Collections.emptyList())
        .build();
  }

  private static String resolveType(Questionnaire questionnaire) {
    return Optional.ofNullable(questionnaire.getName())
        .filter(n -> !n.isBlank())
        .orElseGet(() -> Optional.ofNullable(questionnaire.getIdPart())
            .orElse("unknown"));
  }

  private static String resolveVersion(Questionnaire questionnaire) {
    return Optional.ofNullable(questionnaire.getVersion())
        .filter(v -> !v.isBlank())
        .orElse("1.0");
  }

  private static String resolveName(Questionnaire questionnaire) {
    return Optional.ofNullable(questionnaire.getTitle())
        .filter(t -> !t.isBlank())
        .orElseGet(() -> Optional.ofNullable(questionnaire.getName())
            .orElse("Unnamed Questionnaire"));
  }

  private static String resolveTypeName(Questionnaire questionnaire) {
    return Optional.ofNullable(questionnaire.getName())
        .filter(n -> !n.isBlank())
        .orElseGet(() -> Optional.ofNullable(questionnaire.getIdPart())
            .orElse("UNKNOWN"));
  }

  private static String resolveDescription(Questionnaire questionnaire) {
    return Optional.ofNullable(questionnaire.getDescription())
        .orElse("");
  }

  private static String resolvePurpose(Questionnaire questionnaire) {
    return Optional.ofNullable(questionnaire.getPurpose())
        .orElse("");
  }
}

