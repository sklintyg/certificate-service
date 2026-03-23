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
package se.inera.intyg.certificateservice.domain.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataAction.ACTION_EVALUATION;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.EXTERNAL_REFERENCE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.FK7804_CERTIFICATE_MODEL_ID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PlaceholderCertificateRequest;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateModelRepository;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.event.service.CertificateEventDomainService;
import se.inera.intyg.certificateservice.domain.unit.model.SubUnit;

@ExtendWith(MockitoExtension.class)
class RenewExternalCertificateDomainServiceTest {

  @Mock private CertificateRepository certificateRepository;
  @Mock private CertificateModelRepository certificateModelRepository;
  @Mock private CertificateEventDomainService certificateEventDomainService;
  @Mock private PrefillProcessor prefillProcessor;
  @InjectMocks private RenewExternalCertificateDomainService renewExternalCertificateDomainService;

  @Test
  void shouldUpdateMetadataOnCertificate() {
    final var placeholderRequest =
        PlaceholderCertificateRequest.builder()
            .certificateModelId(FK7804_CERTIFICATE_MODEL_ID)
            .build();
    final var certificate = mock(MedicalCertificate.class);
    final var certificateModel = mock(CertificateModel.class);

    doReturn(certificateModel)
        .when(certificateModelRepository)
        .getActiveById(FK7804_CERTIFICATE_MODEL_ID);
    doReturn(certificate)
        .when(certificateRepository)
        .createFromPlaceholder(placeholderRequest, certificateModel);

    renewExternalCertificateDomainService.renew(
        ACTION_EVALUATION, EXTERNAL_REFERENCE, placeholderRequest);

    verify(certificate).updateMetadata(ACTION_EVALUATION);
  }

  @Test
  void shouldSetExternalReferenceOnRenewCertificate() {
    final var placeholderRequest =
        PlaceholderCertificateRequest.builder()
            .certificateModelId(FK7804_CERTIFICATE_MODEL_ID)
            .build();
    final var certificate = mock(MedicalCertificate.class);
    final var certificateModel = mock(CertificateModel.class);

    doReturn(certificateModel)
        .when(certificateModelRepository)
        .getActiveById(FK7804_CERTIFICATE_MODEL_ID);
    doReturn(certificate)
        .when(certificateRepository)
        .createFromPlaceholder(placeholderRequest, certificateModel);

    renewExternalCertificateDomainService.renew(
        ACTION_EVALUATION, EXTERNAL_REFERENCE, placeholderRequest);

    verify(certificate).externalReference(EXTERNAL_REFERENCE);
  }

  @Test
  void shouldReturnNewCertificate() {
    final var placeholderRequest =
        PlaceholderCertificateRequest.builder()
            .certificateModelId(FK7804_CERTIFICATE_MODEL_ID)
            .build();
    final var certificate = mock(MedicalCertificate.class);
    final var certificateModel = mock(CertificateModel.class);

    doReturn(certificateModel)
        .when(certificateModelRepository)
        .getActiveById(FK7804_CERTIFICATE_MODEL_ID);
    doReturn(certificate)
        .when(certificateRepository)
        .createFromPlaceholder(placeholderRequest, certificateModel);
    doReturn(certificate).when(certificateRepository).save(certificate);

    final var actualCertificate =
        renewExternalCertificateDomainService.renew(
            ACTION_EVALUATION, EXTERNAL_REFERENCE, placeholderRequest);

    assertEquals(certificate, actualCertificate);
  }

  @Test
  void shouldPublishDomainEvent() {
    final var captor = ArgumentCaptor.forClass(CertificateEvent.class);
    final var placeholderRequest =
        PlaceholderCertificateRequest.builder()
            .certificateModelId(FK7804_CERTIFICATE_MODEL_ID)
            .build();
    final var certificate = mock(MedicalCertificate.class);
    final var certificateModel = mock(CertificateModel.class);

    doReturn(certificateModel)
        .when(certificateModelRepository)
        .getActiveById(FK7804_CERTIFICATE_MODEL_ID);
    doReturn(certificate)
        .when(certificateRepository)
        .createFromPlaceholder(placeholderRequest, certificateModel);

    renewExternalCertificateDomainService.renew(
        ACTION_EVALUATION, EXTERNAL_REFERENCE, placeholderRequest);

    verify(certificateEventDomainService).publish(captor.capture());

    assertEquals(CertificateEventType.RENEW, captor.getValue().type());
  }

  @Test
  void shouldUsePrefillXmlToRenewCertificate() {
    final var prefillXml = "<xml>data</xml>";
    final var subUnit = mock(SubUnit.class);
    final var placeholderRequest =
        PlaceholderCertificateRequest.builder()
            .certificateModelId(FK7804_CERTIFICATE_MODEL_ID)
            .prefillXml(new Xml(prefillXml))
            .issuingUnit(subUnit)
            .build();
    final var certificate = mock(MedicalCertificate.class);
    final var certificateModel = mock(CertificateModel.class);

    doReturn(certificateModel)
        .when(certificateModelRepository)
        .getActiveById(FK7804_CERTIFICATE_MODEL_ID);
    doReturn(certificate)
        .when(certificateRepository)
        .createFromPlaceholder(placeholderRequest, certificateModel);

    renewExternalCertificateDomainService.renew(
        ACTION_EVALUATION, EXTERNAL_REFERENCE, placeholderRequest);

    verify(certificate).prefill(placeholderRequest.prefillXml(), prefillProcessor);
  }
}
