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
package se.inera.intyg.certificateservice.testability.certificate.service.fillservice.fk7210;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.CertificateModelFactoryFK7210.FK7210_V1_0;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_ID;
import static se.inera.intyg.certificateservice.testability.certificate.dto.TestabilityFillTypeDTO.EMPTY;
import static se.inera.intyg.certificateservice.testability.certificate.service.fillservice.TestabilityFIllCertificateUtil.elementData;
import static se.inera.intyg.certificateservice.testability.certificate.service.fillservice.TestabilityFIllCertificateUtil.emptyValue;
import static se.inera.intyg.certificateservice.testability.certificate.service.fillservice.TestabilityFIllCertificateUtil.nowPlusDays;
import static se.inera.intyg.certificateservice.testability.certificate.service.fillservice.TestabilityFIllCertificateUtil.spec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.testability.certificate.dto.TestabilityFillTypeDTO;
import se.inera.intyg.certificateservice.testability.certificate.service.fillservice.TestabilityCertificateFillService;

@Component
public class TestabilityCertificateFillServiceFK7210 implements TestabilityCertificateFillService {

  @Override
  public List<CertificateModelId> certificateModelIds() {
    return List.of(FK7210_V1_0);
  }

  @Override
  public List<ElementData> fill(
      CertificateModel certificateModel, TestabilityFillTypeDTO fillType) {

    return fillType == EMPTY ? Collections.emptyList() : fillWithValues(certificateModel);
  }

  private static List<ElementData> fillWithValues(CertificateModel certificateModel) {
    final var elementData = new ArrayList<ElementData>();
    final var specFodelsedatum = spec(QUESTION_BERAKNAT_FODELSEDATUM_ID, certificateModel);

    fodelsedatum(specFodelsedatum, elementData);

    return elementData;
  }

  private static void fodelsedatum(ElementSpecification spec, List<ElementData> list) {
    if (emptyValue(spec) instanceof ElementValueDate elementValueDate) {
      list.add(elementData(spec.id(), elementValueDate.withDate(nowPlusDays(200L))));
    }
  }
}
