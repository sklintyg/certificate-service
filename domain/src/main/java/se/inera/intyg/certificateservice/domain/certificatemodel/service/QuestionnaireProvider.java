package se.inera.intyg.certificateservice.domain.certificatemodel.service;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;

public interface QuestionnaireProvider {

    CertificateModel modelFromQuestionnaire();
}