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
package se.inera.intyg.certificateservice.testability.certificate.service.fillservice;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosisList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

public class TestabilityFIllCertificateUtil {

  private TestabilityFIllCertificateUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification spec(ElementId elementId, CertificateModel certificateModel) {
    return certificateModel.elementSpecification(elementId);
  }

  public static LocalDate now() {
    return LocalDate.now(ZoneId.systemDefault());
  }

  public static LocalDate nowPlusDays(long days) {
    return LocalDate.now(ZoneId.systemDefault()).plusDays(days);
  }

  public static ElementValueDiagnosis valueDiagnosis(
      FieldId fieldId, String code, String desc, String term) {
    return ElementValueDiagnosis.builder()
        .id(fieldId)
        .code(code)
        .description(desc)
        .terminology(term)
        .build();
  }

  public static ElementValueCode valueCode(FieldId codeId, String code) {
    return ElementValueCode.builder().codeId(codeId).code(code).build();
  }

  public static ElementValueDate valueDate(FieldId dateId, LocalDate date) {
    return ElementValueDate.builder().dateId(dateId).date(date).build();
  }

  public static ElementValueText valueText(FieldId fieldId, String text) {
    return ElementValueText.builder().textId(fieldId).text(text).build();
  }

  public static ElementData elementData(ElementId elementId, ElementValue date) {
    return ElementData.builder().id(elementId).value(date).build();
  }

  public static ElementValue emptyValue(ElementSpecification spec) {
    return spec.configuration().emptyValue();
  }

  public static ElementValue diagnosisList(List<ElementValueDiagnosis> diagnoses) {
    return ElementValueDiagnosisList.builder().diagnoses(diagnoses).build();
  }

  public static DateRange dateRange(FieldId fieldId, LocalDate from, LocalDate to) {
    return DateRange.builder().dateRangeId(fieldId).from(from).to(to).build();
  }

  public static MedicalInvestigation investigation(
      FieldId fieldId, ElementValueDate date, ElementValueCode code, ElementValueText text) {
    return MedicalInvestigation.builder()
        .id(fieldId)
        .date(date)
        .investigationType(code)
        .informationSource(text)
        .build();
  }
}
