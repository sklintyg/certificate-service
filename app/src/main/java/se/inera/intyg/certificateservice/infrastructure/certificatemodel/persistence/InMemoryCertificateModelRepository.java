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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.persistence;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateModelRepository;
import se.inera.intyg.certificateservice.domain.common.model.Code;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.CertificateModelFactory;
import se.inera.intyg.certificateservice.testability.certificate.service.repository.TestabilityCertificateModelRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryCertificateModelRepository
    implements CertificateModelRepository, TestabilityCertificateModelRepository {

  private final List<CertificateModelFactory> certificateModelFactories;
  private Map<CertificateModelId, CertificateModel> certificateModelMap;

  @PostConstruct
  private void initCertificateModelMap() {
    log.info("Initiate certificate model repository");
    certificateModelMap = new HashMap<>();
    certificateModelFactories.forEach(
        certificateModelFactory -> {
          final var certificateModel = certificateModelFactory.create();
          certificateModelMap.put(certificateModel.id(), certificateModel);
          log.info("Loaded certificate model '{}' to repository", certificateModel.id());
        });

    final var certificateTypeToVersionReference =
        certificateModelMap.values().stream()
            .sorted(Comparator.comparing(model -> model.id().version().version()))
            .collect(
                Collectors.groupingBy(
                    model -> model.id().type(),
                    Collectors.mapping(Function.identity(), Collectors.toList())));

    certificateModelMap.forEach(
        (key, certificateModel) ->
            certificateModelMap.put(
                key,
                certificateModel.withVersions(
                    certificateTypeToVersionReference.get(certificateModel.id().type()))));
  }

  private Map<CertificateModelId, CertificateModel> getCertificateModelMap() {
    return certificateModelMap;
  }

  @Override
  public List<CertificateModel> findAllActive() {
    return getCertificateModelMap().values().stream()
        .filter(filterActiveCertificateModels())
        .toList();
  }

  @Override
  public Optional<CertificateModel> findLatestActiveByType(CertificateType certificateType) {
    if (certificateType == null) {
      throw new IllegalArgumentException("CertificateType is null!");
    }

    return getCertificateModelMap().values().stream()
        .filter(certificateModel -> certificateType.equals(certificateModel.id().type()))
        .filter(filterActiveCertificateModels())
        .max(Comparator.comparing(CertificateModel::activeFrom));
  }

  @Override
  public Optional<CertificateModel> findLatestActiveByExternalType(Code code) {
    if (code == null) {
      throw new IllegalArgumentException("Code is null!");
    }

    return getCertificateModelMap().values().stream()
        .filter(certificateModel -> code.matches(certificateModel.type()))
        .filter(filterActiveCertificateModels())
        .max(Comparator.comparing(CertificateModel::activeFrom));
  }

  @Override
  public CertificateModel getById(CertificateModelId certificateModelId) {
    if (certificateModelId == null) {
      throw new IllegalArgumentException("CertificateModelId is null!");
    }

    final var certificateModel = getCertificateModelMap().get(certificateModelId);
    if (certificateModel == null) {
      throw new IllegalArgumentException(
          "CertificateModel missing: %s".formatted(certificateModelId));
    }

    return certificateModel;
  }

  @Override
  public CertificateModel getActiveById(CertificateModelId certificateModelId) {
    if (certificateModelId == null) {
      throw new IllegalArgumentException("CertificateModelId is null!");
    }

    final var certificateModel = getCertificateModelMap().get(certificateModelId);
    if (certificateModel == null) {
      throw new IllegalArgumentException(
          "CertificateModel missing: %s".formatted(certificateModelId));
    }

    if (LocalDateTime.now(ZoneId.systemDefault()).isBefore(certificateModel.activeFrom())) {
      throw new IllegalArgumentException(
          "CertificateModel with id '%s' not active until '%s'"
              .formatted(certificateModel.id(), certificateModel.activeFrom()));
    }

    return certificateModel;
  }

  @Override
  public List<CertificateModel> all() {
    return getCertificateModelMap().values().stream().toList();
  }

  private static Predicate<CertificateModel> filterActiveCertificateModels() {
    return certificateModel ->
        certificateModel.activeFrom().isBefore(LocalDateTime.now(ZoneId.systemDefault()));
  }
}
