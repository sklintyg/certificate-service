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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.TextFieldAppearance;

@ExtendWith(MockitoExtension.class)
class TextFieldAppearanceFactoryTest {

  @Test
  void shouldCreateTextFieldAppearanceForTextField() {
    final var pdTextField = new PDTextField(mock(PDAcroForm.class));
    final var expected = new TextFieldAppearance(pdTextField);

    final var actual = new TextFieldAppearanceFactory().create(pdTextField).orElseThrow();

    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnEmptyForNonTextField() {
    final var pdCheckBox = new PDCheckBox(mock(PDAcroForm.class));

    final var actual = new TextFieldAppearanceFactory().create(pdCheckBox);

    assertFalse(actual.isPresent());
  }
}
