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
package se.inera.intyg.certificateservice.domain.certificate.repository;

import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateExportPage;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.PlaceholderCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PlaceholderCertificateRequest;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;

public interface CertificateRepository {

  Certificate create(CertificateModel certificateModel);

  Certificate createFromPlaceholder(PlaceholderCertificateRequest request, CertificateModel model);

  Certificate save(Certificate certificate);

  Certificate getById(CertificateId certificateId);

  List<Certificate> getByIds(List<CertificateId> certificateIds);

  List<Certificate> findByIds(List<CertificateId> certificateIds);

  boolean exists(CertificateId certificateId);

  List<Certificate> findByCertificatesRequest(CertificatesRequest request);

  List<CertificateId> findIdsByCreatedBeforeAndStatusIn(CertificatesRequest request);

  CertificateExportPage getExportByCareProviderId(HsaId careProviderId, int page, int size);

  long deleteByCareProviderId(HsaId careProviderId);

  boolean placeholderExists(CertificateId certificateId);

  PlaceholderCertificate getPlaceholderById(CertificateId certificateId);

  PlaceholderCertificate save(PlaceholderCertificate placeholderCertificate);

  CertificateMetaData getMetadataFromSignInstance(
      CertificateMetaData certificateMetaData, LocalDateTime signed);

  List<CertificateId> findValidSickLeavesCertificateIdsByIds(List<CertificateId> certificateId);

  void updateCertificateMetadataFromSignInstances(List<Certificate> certificates);

  void remove(List<CertificateId> certificateIds);

  Long getNumberOfSignedCertificatesIssuedBy(HsaId hsaId);
}
