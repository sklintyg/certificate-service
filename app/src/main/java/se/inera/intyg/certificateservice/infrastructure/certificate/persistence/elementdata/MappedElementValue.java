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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = MappedElementValueDate.class, name = "DATE"),
  @JsonSubTypes.Type(value = MappedElementValueIssuingUnit.class, name = "ISSUING_UNIT"),
  @JsonSubTypes.Type(value = MappedElementValueText.class, name = "TEXT"),
  @JsonSubTypes.Type(value = MappedElementValueDateRangeList.class, name = "DATE_RANGE_LIST"),
  @JsonSubTypes.Type(value = MappedElementValueDateList.class, name = "DATE_LIST"),
  @JsonSubTypes.Type(value = MappedElementValueCode.class, name = "CODE"),
  @JsonSubTypes.Type(value = MappedElementValueBoolean.class, name = "BOOLEAN"),
  @JsonSubTypes.Type(value = MappedElementValueDiagnosisList.class, name = "DIAGNOSIS_LIST"),
  @JsonSubTypes.Type(
      value = MappedElementValueMedicalInvestigationList.class,
      name = "MEDICAL_INVESTIGATION_LIST"),
  @JsonSubTypes.Type(value = MappedElementValueCodeList.class, name = "CODE_LIST"),
  @JsonSubTypes.Type(value = MappedElementValueVisualAcuities.class, name = "VISUAL_ACUITIES"),
  @JsonSubTypes.Type(value = MappedElementValueVisualAcuity.class, name = "VISUAL_ACUITY"),
  @JsonSubTypes.Type(value = MappedElementValueDouble.class, name = "DOUBLE"),
  @JsonSubTypes.Type(value = MappedElementValueDateRange.class, name = "DATE_RANGE"),
  @JsonSubTypes.Type(value = MappedElementValueInteger.class, name = "INTEGER"),
  @JsonSubTypes.Type(value = MappedElementValueIcf.class, name = "ICF"),
})
public interface MappedElementValue {}
