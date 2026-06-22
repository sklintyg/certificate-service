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

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;

@Value
@Builder
public class PdfConfigurationDateRange implements PdfConfiguration {

  PdfFieldId from;
  PdfFieldId to;

  @Override
  public Stream<PdfField> toPdfFields(
      ElementSpecification elementSpec,
      Certificate certificate,
      CustomPdfSpecification pdfSpecification) {
    return elementSpec.valueAs(certificate, ElementValueDateRange.class).stream()
        .flatMap(
            value ->
                Stream.concat(
                    Optional.ofNullable(value.fromDate()).stream()
                        .map(
                            date ->
                                PdfField.builder().fieldId(from).value(date.toString()).build()),
                    Optional.ofNullable(value.toDate()).stream()
                        .map(
                            date ->
                                PdfField.builder().fieldId(to).value(date.toString()).build())));
  }
}
