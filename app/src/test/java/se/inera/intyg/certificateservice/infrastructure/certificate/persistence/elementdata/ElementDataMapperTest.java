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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.elementdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

@ExtendWith(MockitoExtension.class)
class ElementDataMapperTest {

  private static final String ID = "id";
  @Mock private ElementValueMapperText elementValueMapperText;
  private ElementDataMapper elementDataMapper;

  @BeforeEach
  void setUp() {
    elementDataMapper = new ElementDataMapper(List.of(elementValueMapperText));
  }

  @Nested
  class ToDomainTest {

    @Test
    void shallThrowIfNoConverterSupportsClass() {
      final var elementData =
          MappedElementData.builder()
              .id(ID)
              .value(MappedElementValueText.builder().build())
              .build();

      doReturn(false).when(elementValueMapperText).supports(elementData.getValue().getClass());

      assertThrows(IllegalStateException.class, () -> elementDataMapper.toDomain(elementData));
    }

    @Test
    void shallConvertId() {
      final var expectedId = new ElementId(ID);
      final var elementData =
          MappedElementData.builder()
              .id(ID)
              .value(MappedElementValueText.builder().build())
              .build();

      final var elementValue = ElementValueText.builder().build();

      doReturn(true).when(elementValueMapperText).supports(elementData.getValue().getClass());
      doReturn(elementValue).when(elementValueMapperText).toDomain(elementData.getValue());

      final var result = elementDataMapper.toDomain(elementData);
      assertEquals(expectedId, result.id());
    }

    @Test
    void shallConvertValue() {
      final var elementData =
          MappedElementData.builder()
              .id(ID)
              .value(MappedElementValueText.builder().build())
              .build();

      final var expectedValue = ElementValueText.builder().build();

      doReturn(true).when(elementValueMapperText).supports(elementData.getValue().getClass());
      doReturn(expectedValue).when(elementValueMapperText).toDomain(elementData.getValue());

      final var result = elementDataMapper.toDomain(elementData);
      assertEquals(expectedValue, result.value());
    }
  }

  @Nested
  class ToMappedTest {

    @Test
    void shallThrowIfNoConverterSupportsClass() {
      final var elementData =
          ElementData.builder()
              .id(new ElementId(ID))
              .value(ElementValueText.builder().build())
              .build();

      doReturn(false).when(elementValueMapperText).supports(elementData.value().getClass());

      assertThrows(IllegalStateException.class, () -> elementDataMapper.toMapped(elementData));
    }

    @Test
    void shallConvertId() {
      final var elementData =
          ElementData.builder()
              .id(new ElementId(ID))
              .value(ElementValueText.builder().build())
              .build();

      final var elementValue = MappedElementValueText.builder().build();

      doReturn(true).when(elementValueMapperText).supports(elementData.value().getClass());
      doReturn(elementValue).when(elementValueMapperText).toMapped(elementData.value());

      final var result = elementDataMapper.toMapped(elementData);
      assertEquals(ID, result.getId());
    }

    @Test
    void shallConvertValue() {
      final var elementData =
          ElementData.builder()
              .id(new ElementId(ID))
              .value(ElementValueText.builder().build())
              .build();

      final var expectedValue = MappedElementValueText.builder().build();

      doReturn(true).when(elementValueMapperText).supports(elementData.value().getClass());
      doReturn(expectedValue).when(elementValueMapperText).toMapped(elementData.value());

      final var result = elementDataMapper.toMapped(elementData);
      assertEquals(expectedValue, result.getValue());
    }
  }
}
