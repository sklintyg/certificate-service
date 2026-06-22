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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FK3221TemplatePathProviderTest {

  public static final String PDF_FK_3221_PDF = "fk3221/pdf/fk3221_v1.pdf";
  public static final String PDF_NO_ADDRESS_FK_3221_PDF = "fk3221/pdf/fk3221_v1_no_address.pdf";

  private final FK3221TemplatePathProvider provider = new FK3221TemplatePathProvider();

  @Test
  void shallReturnTemplatePathWithNoAddress() {
    assertEquals(PDF_NO_ADDRESS_FK_3221_PDF, provider.pathWithoutAddress());
  }

  @Test
  void shallReturnTemplatePathWithAddress() {
    assertEquals(PDF_FK_3221_PDF, provider.pathWithAddress());
  }
}
