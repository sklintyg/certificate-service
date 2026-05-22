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
package se.inera.intyg.certificateservice.domain.validation.model;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Value
@Builder
public class ElementValidationUnitContactInformation implements ElementValidation {

  private static final ElementId CATEGORY_ELEMENT_ID = new ElementId("vardenhet");
  private static final FieldId ADDRESS_FIELD_ID =
      new FieldId("grunddata.skapadAv.vardenhet.postadress");
  private static final String ADDRESS_MESSAGE = "Ange postadress.";
  private static final FieldId ZIP_CODE_FIELD_ID =
      new FieldId("grunddata.skapadAv.vardenhet.postnummer");
  private static final String ZIP_CODE_MESSAGE = "Ange postnummer.";
  private static final FieldId CITY_FIELD_ID = new FieldId("grunddata.skapadAv.vardenhet.postort");
  private static final String CITY_MESSAGE = "Ange postort.";
  private static final FieldId PHONE_NUMBER_FIELD_ID =
      new FieldId("grunddata.skapadAv.vardenhet.telefonnummer");
  private static final String PHONE_NUMBER_MESSAGE = "Ange telefonnummer.";

  @Override
  public List<ValidationError> validate(
      ElementData data, Optional<ElementId> categoryId, List<ElementData> dataList) {
    if (data == null) {
      throw new IllegalArgumentException("Element data is null");
    }

    final var elementValueUnitContactInformation = getValue(data.value());

    final var validationErrors = new ArrayList<ValidationError>();
    if (isNullOrBlank(elementValueUnitContactInformation.address())) {
      validationErrors.add(errorMessage(ADDRESS_FIELD_ID, ADDRESS_MESSAGE));
    }

    if (isNullOrBlank(elementValueUnitContactInformation.zipCode())) {
      validationErrors.add(errorMessage(ZIP_CODE_FIELD_ID, ZIP_CODE_MESSAGE));
    }

    if (isNullOrBlank(elementValueUnitContactInformation.city())) {
      validationErrors.add(errorMessage(CITY_FIELD_ID, CITY_MESSAGE));
    }

    if (isNullOrBlank(elementValueUnitContactInformation.phoneNumber())) {
      validationErrors.add(errorMessage(PHONE_NUMBER_FIELD_ID, PHONE_NUMBER_MESSAGE));
    }

    return validationErrors;
  }

  private static boolean isNullOrBlank(String value) {
    return value == null || value.isBlank();
  }

  private ElementValueUnitContactInformation getValue(ElementValue value) {
    if (value == null) {
      throw new IllegalArgumentException("Element data value is null");
    }

    if (value instanceof ElementValueUnitContactInformation elementValueUnitContactInformation) {
      return elementValueUnitContactInformation;
    }

    throw new IllegalArgumentException(
        "Element data value %s is of wrong type".formatted(value.getClass()));
  }

  private static ValidationError errorMessage(FieldId fieldId, String message) {
    return ValidationError.builder()
        .elementId(UNIT_CONTACT_INFORMATION)
        .fieldId(fieldId)
        .categoryId(CATEGORY_ELEMENT_ID)
        .message(new ErrorMessage(message))
        .build();
  }
}
