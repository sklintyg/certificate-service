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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.common;

import java.util.List;
import java.util.function.Predicate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRangeList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

public class ElementDataPredicateFactory {

  private ElementDataPredicateFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static Predicate<List<ElementData>> valueBoolean(ElementId elementId) {
    return valueBoolean(elementId, true);
  }

  public static Predicate<List<ElementData>> radioBooleans(List<ElementId> elementIds) {
    return radioBooleans(elementIds, true);
  }

  public static Predicate<List<ElementData>> valueBoolean(
      ElementId elementId, boolean expectedValue) {
    return radioBooleans(List.of(elementId), expectedValue);
  }

  public static Predicate<List<ElementData>> checkboxBoolean(
      ElementId elementId, final boolean expectedValue) {
    return elementData -> {
      final var matches =
          elementData.stream()
              .filter(data -> elementId.equals(data.id()))
              .filter(element -> element.value() != null)
              .map(element -> (ElementValueBoolean) element.value())
              .filter(elementValueBoolean -> elementValueBoolean.value() != null)
              .toList();

      if (!expectedValue && matches.isEmpty()) {
        return true;
      }

      return matches.stream()
          .anyMatch(
              value -> value != null && value.value() != null && value.value() == expectedValue);
    };
  }

  public static Predicate<List<ElementData>> radioBooleans(
      final List<ElementId> elementIds, final boolean expectedValue) {
    return elementData ->
        elementData.stream()
            .filter(data -> elementIds.contains(data.id()))
            .map(element -> (ElementValueBoolean) element.value())
            .anyMatch(
                value -> value != null && value.value() != null && value.value() == expectedValue);
  }

  public static Predicate<List<ElementData>> codes(ElementId elementId, List<FieldId> fieldIds) {
    return elementData ->
        elementData.stream()
            .filter(data -> data.id().equals(elementId))
            .map(element -> (ElementValueCode) element.value())
            .anyMatch(value -> fieldIds.contains(value.codeId()));
  }

  public static Predicate<List<ElementData>> codeList(ElementId elementId, List<FieldId> fieldIds) {
    return elementData ->
        elementData.stream()
            .filter(data -> data.id().equals(elementId))
            .map(element -> (ElementValueCodeList) element.value())
            .map(ElementValueCodeList::list)
            .flatMap(List::stream)
            .anyMatch(value -> fieldIds.contains(value.codeId()));
  }

  public static Predicate<List<ElementData>> dateRangeList(
      ElementId elementId, List<FieldId> fieldIds) {
    return elementData ->
        elementData.stream()
            .filter(data -> data.id().equals(elementId))
            .map(element -> (ElementValueDateRangeList) element.value())
            .anyMatch(
                value ->
                    value.dateRangeList().stream()
                        .anyMatch(valueDate -> fieldIds.contains(valueDate.dateRangeId())));
  }

  public static Predicate<List<ElementData>> visualAcuities(
      ElementId elementId, double upperLimit, double lowerLimit) {
    return elementData ->
        elementData.stream()
            .filter(data -> data.id().equals(elementId))
            .map(data -> (ElementValueVisualAcuities) data.value())
            .anyMatch(
                visualAcuities ->
                    (visualAcuities.rightEye().withoutCorrection().value() != null
                            && visualAcuities.rightEye().withoutCorrection().value() < upperLimit
                            && visualAcuities.leftEye().withoutCorrection().value() != null
                            && visualAcuities.leftEye().withoutCorrection().value() < upperLimit)
                        || ((visualAcuities.rightEye().withoutCorrection().value() != null
                                && visualAcuities.rightEye().withoutCorrection().value()
                                    < lowerLimit)
                            || (visualAcuities.leftEye().withoutCorrection().value() != null
                                && visualAcuities.leftEye().withoutCorrection().value()
                                    < lowerLimit)));
  }
}
