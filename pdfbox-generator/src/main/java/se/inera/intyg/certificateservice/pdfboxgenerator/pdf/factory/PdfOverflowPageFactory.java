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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.factory;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.CertificatePdfContext;

@Component
public class PdfOverflowPageFactory {

  public PDPage create(CertificatePdfContext context) {
    final var document = context.getDocument();
    final var templateSpec = context.getTemplatePdfSpecification();

    final var templatePage = document.getPage(templateSpec.overFlowPageIndex().value());
    final var dictionary = new COSDictionary(templatePage.getCOSObject());
    dictionary.removeItem(COSName.ANNOTS);

    return new PDPage(dictionary);
  }
}
