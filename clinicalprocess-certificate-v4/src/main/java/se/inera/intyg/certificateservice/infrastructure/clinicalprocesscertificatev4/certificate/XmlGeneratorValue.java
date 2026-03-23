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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate;

import jakarta.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CustomMapperId;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.common.XmlMapping;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

@Component
public class XmlGeneratorValue {

  private final Map<Class<? extends ElementValue>, XmlGenerator> xmlGeneratorElementDataMap;
  private final Map<CustomMapperId, XmlGenerator> xmlGeneratorCustomMap;

  public XmlGeneratorValue(
      List<XmlGeneratorElementValue> xmlGeneratorElementValueMap,
      List<XmlGeneratorCustomMapper> xmlGeneratorCustomMappers) {
    this.xmlGeneratorElementDataMap =
        xmlGeneratorElementValueMap.stream()
            .collect(Collectors.toMap(XmlGeneratorElementValue::supports, Function.identity()));
    this.xmlGeneratorCustomMap =
        xmlGeneratorCustomMappers.stream()
            .collect(Collectors.toMap(XmlGeneratorCustomMapper::id, Function.identity()));
  }

  public List<Svar> generate(Certificate certificate) {
    final var answerList = new ArrayList<Svar>();

    mappings(certificate.certificateModel(), certificate.elementData())
        .forEach(
            mapping -> {
              if (mapping.getAnswers().isEmpty()) {
                return;
              }

              if (noCustomMapping(mapping)) {
                answerList.addAll(mapping.getAnswers());
                return;
              }

              final var answerToMapTo = answerToMapTo(mapping, answerList);
              answerToMapTo
                  .getDelsvar()
                  .addAll(
                      mapping.getAnswers().stream()
                          .map(answer -> answer.getDelsvar().stream().toList())
                          .flatMap(List::stream)
                          .toList());
            });

    return answerList;
  }

  private List<XmlMapping> mappings(
      CertificateModel certificateModel, List<ElementData> elementData) {
    return elementData.stream()
        .filter(data -> !(data.value() instanceof ElementValueUnitContactInformation))
        .filter(data -> certificateModel.elementSpecification(data.id()).includeInXml())
        .sorted((o1, o2) -> certificateModel.compare(o1.id(), o2.id()))
        .map(
            data -> {
              final var customMapperId =
                  certificateModel
                      .elementSpecification(data.id())
                      .getMapping()
                      .flatMap(ElementMapping::customMapperId);

              final var converter =
                  customMapperId
                      .map(xmlGeneratorCustomMap::get)
                      .orElseGet(() -> xmlGeneratorElementDataMap.get(data.value().getClass()));

              if (converter == null) {
                throw new IllegalStateException(
                    "Converter for '%s' not found".formatted(data.value().getClass()));
              }

              return XmlMapping.builder()
                  .mapping(certificateModel.elementSpecification(data.id()).mapping())
                  .answers(
                      converter.generate(data, certificateModel.elementSpecification(data.id())))
                  .build();
            })
        .toList();
  }

  private static boolean noCustomMapping(XmlMapping mapping) {
    return mapping.getMapping() == null || mapping.getMapping().elementId() == null;
  }

  private static Svar answerToMapTo(XmlMapping mapping, ArrayList<Svar> answerList) {
    return answerList.stream()
        .filter(svar -> svar.getId().equalsIgnoreCase(mapping.getMapping().elementId().id()))
        .filter(
            svar ->
                mapping.getMapping().code() == null
                    || svar.getDelsvar().stream()
                        .anyMatch(
                            delsvar ->
                                delsvar.getContent().stream()
                                    .filter(JAXBElement.class::isInstance)
                                    .map(JAXBElement.class::cast)
                                    .map(JAXBElement::getValue)
                                    .filter(CVType.class::isInstance)
                                    .map(CVType.class::cast)
                                    .anyMatch(
                                        cvType ->
                                            cvType
                                                .getCode()
                                                .equalsIgnoreCase(
                                                    mapping.getMapping().code().code()))))
        .findAny()
        .orElseGet(
            () -> {
              final var emptySvar = new Svar();
              emptySvar.setId(mapping.getMapping().elementId().id());
              answerList.add(emptySvar);
              return emptySvar;
            });
  }
}
