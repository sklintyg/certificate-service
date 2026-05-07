package se.inera.intyg.certificateservice.infrastructure.certificatemodel.questionnaire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.service.QuestionnaireProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.CertificateModelFactory;

@Component
@RequiredArgsConstructor
public class CertificateModelFactoryQuestionnaire implements CertificateModelFactory {

  private final QuestionnaireProvider questionnaireProvider;

  @Override
  public CertificateModel create() {
    return questionnaireProvider.modelFromQuestionnaire();
  }
}