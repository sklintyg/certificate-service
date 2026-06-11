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

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * PDF specification for template-based PDF generation that is delegated to
 * certificate-print-service (CPS) via its custom print endpoint. The caller supplies the PDF
 * template together with a map of form-field values and overlay metadata, and CPS performs the
 * actual rendering.
 */
@Value
@Builder
public class CustomPdfSpecification implements PdfSpecification {

  String pdfTemplatePath;
  String pdfNoAddressTemplatePath;
  List<PdfFieldId> patientIdFieldIds;
  PdfSignature signature;
  int signatureTextX;
  int signatureTextY;
  int signatureTextFontSize;
}
