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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;

class CitizenPdfConfigurationTest {

  private static final ElementSimplifiedValueText REPLACEMENT =
      ElementSimplifiedValueText.builder().text("REPLACEMENT").build();
  private static final ElementId HIDDEN_BY = new ElementId("HIDDEN_BY");

  @Test
  void shouldReturnEmptyIfNotCitizenFormat() {
    final var config = CitizenPdfConfiguration.builder().hiddenBy(HIDDEN_BY).build();

    final var options =
        PdfGeneratorOptions.builder()
            .citizenFormat(false)
            .hiddenElements(Collections.emptyList())
            .build();

    final var result = config.replacementElementValue(options, Collections.emptyList());

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnReplacementValueIfHiddenByIsInHiddenElements() {
    final var config =
        CitizenPdfConfiguration.builder().hiddenBy(HIDDEN_BY).replacementValue(REPLACEMENT).build();

    final var options =
        PdfGeneratorOptions.builder()
            .citizenFormat(true)
            .hiddenElements(List.of(HIDDEN_BY))
            .build();

    final var result = config.replacementElementValue(options, Collections.emptyList());

    assertTrue(result.isPresent());
  }

  @Test
  void shouldReturnReplacementValueIfShouldHidePredicateIsTrue() {
    final var config =
        CitizenPdfConfiguration.builder()
            .shouldHide(elementData -> true)
            .replacementValue(REPLACEMENT)
            .build();

    final var options =
        PdfGeneratorOptions.builder()
            .citizenFormat(true)
            .hiddenElements(Collections.emptyList())
            .build();

    final var result = config.replacementElementValue(options, Collections.emptyList());

    assertTrue(result.isPresent());
  }

  @Test
  void shouldReturnEmptyIfNeitherHiddenByNorShouldHidePredicateIsTrue() {
    final var config =
        CitizenPdfConfiguration.builder()
            .hiddenBy(HIDDEN_BY)
            .shouldHide(elementData -> false)
            .replacementValue(REPLACEMENT)
            .build();

    final var options =
        PdfGeneratorOptions.builder()
            .citizenFormat(true)
            .hiddenElements(List.of(new ElementId("otherElement")))
            .build();

    final var result = config.replacementElementValue(options, Collections.emptyList());

    assertTrue(result.isEmpty());
  }
}
