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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804;

import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTemplatePathProvider;

public class FK7804TemplatePathProvider implements PdfTemplatePathProvider {

  public static final String PDF_FK_7804_PDF = "fk7804/pdf/fk7804_v2.pdf";
  public static final String PDF_NO_ADDRESS_FK_7804_PDF = "fk7804/pdf/fk7804_v2_no_address.pdf";

  @Override
  public String pathOf(Certificate certificate, PdfGeneratorOptions options) {
    return includeAddress(certificate, options) ? PDF_FK_7804_PDF : PDF_NO_ADDRESS_FK_7804_PDF;
  }

  private static boolean includeAddress(Certificate certificate, PdfGeneratorOptions options) {
    if (options.citizenFormat()) {
      return false;
    }
    return certificate.sent() == null || certificate.sent().sentAt() == null;
  }
}
