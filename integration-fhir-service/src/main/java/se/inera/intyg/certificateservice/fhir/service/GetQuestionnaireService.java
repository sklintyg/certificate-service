package se.inera.intyg.certificateservice.fhir.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.fhir.integration.FHIRIntegrationService;

@Service
@RequiredArgsConstructor
public class GetQuestionnaireService {

  private final FHIRIntegrationService fhirIntegrationService;

  public CertificateModel questionnaireFromQuestionnaire() {
    final var questionnaire = fhirIntegrationService.getQuestionnaire();
    return null;
  }
}