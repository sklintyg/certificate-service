package se.inera.intyg.certificateservice.fhir;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.service.QuestionnaireProvider;
import se.inera.intyg.certificateservice.fhir.service.GetQuestionnaireService;

@Service
@RequiredArgsConstructor
public class QuestionnaireProviderService implements QuestionnaireProvider {

  private final GetQuestionnaireService getQuestionnaireService;

  @Override
  public CertificateModel modelFromQuestionnaire() {
    return getQuestionnaireService.questionnaireFromQuestionnaire();
  }
}