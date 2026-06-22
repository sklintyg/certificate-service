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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.AbstractTemplatePathProvider;

public class FK7426TemplatePathProvider extends AbstractTemplatePathProvider {

  public static final String PDF_FK_7426_PDF = "fk7426/pdf/fk7426_v1.pdf";
  public static final String PDF_NO_ADDRESS_FK_7426_PDF = "fk7426/pdf/fk7426_v1_no_address.pdf";

  @Override
  protected String pathWithoutAddress() {
    return PDF_NO_ADDRESS_FK_7426_PDF;
  }

  @Override
  protected String pathWithAddress() {
    return PDF_FK_7426_PDF;
  }
}
