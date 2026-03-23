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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.factory.TextFieldAppearanceFactory;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.text.OverFlowLineSplit;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.text.TextUtil;

@Slf4j
@RequiredArgsConstructor
@Component
public class PdfPaginationUtil {

  private final TextUtil textUtil;

  private final TextFieldAppearanceFactory textFieldAppearanceFactory;

  public List<List<PdfField>> paginateFields(
      CertificatePdfContext context, List<PdfField> appendedFields, PDField overflowField) {

    final var textFieldAppearance =
        textFieldAppearanceFactory
            .create(overflowField)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Overflow field is not a variable text field: "
                            + overflowField.getFullyQualifiedName()));

    List<List<PdfField>> pages = new ArrayList<>();
    List<PdfField> currentPage = new ArrayList<>();

    for (PdfField field : appendedFields) {

      final var overflow =
          textUtil.getOverflowingLines(
              currentPage,
              field,
              overflowField.getWidgets().getFirst().getRectangle(),
              textFieldAppearance.getFontSize(),
              textFieldAppearance.getFont(context.getAcroForm().getDefaultResources()));

      if (overflow.isEmpty()) {
        currentPage.add(field);
        continue;
      }

      FieldSplit split = splitField(field, overflow.get());
      currentPage.add(split.first());

      pages.add(currentPage);

      final var overflows = split.overflows();

      if (overflows.isEmpty()) {
        currentPage = new ArrayList<>();
      } else {
        pages.addAll(overflows.subList(0, overflows.size() - 1));

        currentPage = new ArrayList<>(overflows.getLast());
      }
    }

    if (!currentPage.isEmpty()) {
      pages.add(currentPage);
    }

    return pages;
  }

  private FieldSplit splitField(PdfField original, OverFlowLineSplit parts) {

    PdfField first = null;

    if (parts.partOne() != null) {
      first = original.withValue(parts.partOne());
    }

    final var overflowingFields =
        parts.overflowPages().stream()
            .map(
                text ->
                    PdfField.builder()
                        .id(original.getId())
                        .value(text)
                        .appearance(original.getAppearance())
                        .append(true)
                        .build())
            .map(List::of)
            .toList();

    return new FieldSplit(first, overflowingFields);
  }
}
