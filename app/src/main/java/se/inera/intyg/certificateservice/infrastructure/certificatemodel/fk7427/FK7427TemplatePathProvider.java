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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.AbstractTemplatePathProvider;

public class FK7427TemplatePathProvider extends AbstractTemplatePathProvider {

  private static final String PDF_FK_7427_PDF = "fk7427/pdf/fk7427_v1.pdf";
  private static final String PDF_FK_7427_PDF_NO_ADDRESS = "fk7427/pdf/fk7427_v1_no_address.pdf";

  @Override
  protected String pathWithoutAddress() {
    return PDF_FK_7427_PDF_NO_ADDRESS;
  }

  @Override
  protected String pathWithAddress() {
    return PDF_FK_7427_PDF;
  }
}
