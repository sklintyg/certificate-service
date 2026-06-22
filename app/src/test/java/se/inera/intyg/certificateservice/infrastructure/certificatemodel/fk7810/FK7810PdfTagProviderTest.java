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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;

@ExtendWith(MockitoExtension.class)
class FK7810PdfTagProviderTest {

  private static final PdfTagIndex EXPECTED_TAG_INDEX = new PdfTagIndex(24);

  @Mock private Certificate certificate;

  private final FK7810PdfTagProvider provider = new FK7810PdfTagProvider();

  private PdfGeneratorOptions options(boolean citizenFormat) {
    return PdfGeneratorOptions.builder()
        .additionalInfoText("Webcert")
        .citizenFormat(citizenFormat)
        .hiddenElements(List.of())
        .build();
  }

  @Test
  void shallReturnTagIndexWhenNotCitizenAndNotSent() {
    assertEquals(EXPECTED_TAG_INDEX, provider.of(certificate, options(false)));
  }

  @Test
  void shallReturnTagIndexWhenCitizenFormat() {
    assertEquals(EXPECTED_TAG_INDEX, provider.of(certificate, options(true)));
  }

  @Test
  void shallReturnTagIndexWhenSent() {
    assertEquals(EXPECTED_TAG_INDEX, provider.of(certificate, options(false)));
  }
}
