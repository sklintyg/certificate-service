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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.CustomMapperId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.converter.PrefillConverter;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.converter.PrefillCustomConverter;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.converter.PrefillStandardConverter;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

@Slf4j
@Component
public class PrefillHandler {

  private static final List<ElementType> IGNORED_TYPES =
      List.of(ElementType.CATEGORY, ElementType.MESSAGE, ElementType.ISSUING_UNIT);

  private final Map<Class<? extends ElementConfiguration>, PrefillConverter> converters;
  private final Map<CustomMapperId, PrefillConverter> customConverters;

  public PrefillHandler(
      List<PrefillStandardConverter> converters, List<PrefillCustomConverter> customConverters) {
    this.converters =
        converters.stream()
            .collect(Collectors.toMap(PrefillStandardConverter::supports, Function.identity()));
    this.customConverters =
        customConverters.stream()
            .collect(Collectors.toMap(PrefillCustomConverter::id, Function.identity()));
  }

  public Collection<PrefillAnswer> unknownAnswerIds(CertificateModel model, Forifyllnad prefill) {
    return prefill.getSvar().stream()
        .filter(s -> !model.elementSpecificationExists(new ElementId(s.getId())))
        .map(sp -> PrefillAnswer.answerNotFound(sp.getId()))
        .toList();
  }

  public Collection<PrefillAnswer> handlePrefill(
      CertificateModel certificateModel, Forifyllnad prefill) {
    return certificateModel.elementSpecifications().stream()
        .flatMap(ElementSpecification::flatten)
        .filter(isQuestion())
        .map(
            elementSpecification -> {
              final var customMapperId =
                  elementSpecification.getMapping().flatMap(ElementMapping::customMapperId);

              final var converter =
                  customMapperId
                      .map(customConverters::get)
                      .orElseGet(
                          () -> converters.get(elementSpecification.configuration().getClass()));

              if (converter == null) {
                throw new IllegalStateException(
                    "Converter for '%s' not found"
                        .formatted(elementSpecification.configuration().getClass()));
              }
              try {
                return converter.prefillAnswer(elementSpecification, prefill);
              } catch (Exception ex) {
                log.error(
                    "Error while pre-filling element with id '{}': {}",
                    elementSpecification.id().id(),
                    ex.getMessage(),
                    ex);
                return PrefillAnswer.builder()
                    .errors(List.of(PrefillError.technicalError(elementSpecification.id().id())))
                    .build();
              }
            })
        .filter(Objects::nonNull)
        .toList();
  }

  private static Predicate<ElementSpecification> isQuestion() {
    return e -> !IGNORED_TYPES.contains(e.configuration().type());
  }
}
