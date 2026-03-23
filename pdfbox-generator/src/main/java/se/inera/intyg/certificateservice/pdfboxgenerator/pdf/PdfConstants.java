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

public class PdfConstants {

  private PdfConstants() {
    throw new IllegalStateException("Utility class");
  }

  // TEXT
  public static final String WATERMARK_DRAFT = "UTKAST";
  public static final String DIGITALLY_SIGNED_TEXT =
      "Detta är en utskrift av ett elektroniskt intyg. "
          + "Intyget har signerats elektroniskt av intygsutfärdaren.";

  // VALUES
  public static final String CHECKED_BOX_VALUE = "1";
  public static final String UNCHECKED_BOX_VALUE = "Off";

  public static final float TEXT_FIELD_LINE_HEIGHT = 1.4F;
  public static final int Y_MARGIN_APPENDIX_PAGE = 16;
  public static final int X_MARGIN_APPENDIX_PAGE = 2;
}
