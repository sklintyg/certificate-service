package se.inera.intyg.certificateservice.fhir;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemComponent;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionFactory;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateTypeName;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateVersion;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.common.model.Role;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

@Component
@RequiredArgsConstructor
public class QuestionnaireToCertificateModelMapper {

  private final CertificateActionFactory certificateActionFactory;

  public CertificateModel map(Questionnaire questionnaire) {
    final var signingRoles = List.of(Role.DOCTOR, Role.PRIVATE_DOCTOR);
    final var nonSigningRoles = List.of(Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE);
    final var allowedRoles =
        Stream.concat(signingRoles.stream(), nonSigningRoles.stream()).toList();

    return CertificateModel.builder()
        .id(mapModelId(questionnaire))
        .typeName(mapTypeName(questionnaire))
        .name(questionnaire.getTitle())
        .description(questionnaire.getDescription())
        .detailedDescription(mapDetailedDescription(questionnaire))
        .activeFrom(mapActiveFrom(questionnaire))
        .certificateActionSpecifications(Collections.emptyList())
        .certificateActionFactory(certificateActionFactory)
        .certificateActionSpecifications(
            List.of(
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.CREATE)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.READ)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.UPDATE)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.DELETE)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.SIGN)
                    .allowedRoles(signingRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.SEND)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.REVOKE)
                    .allowedRoles(signingRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.REPLACE)
                    .allowedRoles(signingRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.REPLACE_CONTINUE)
                    .allowedRoles(signingRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.RENEW)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.FORWARD_MESSAGE)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.FORWARD_CERTIFICATE)
                    .allowedRoles(nonSigningRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.READY_FOR_SIGN)
                    .allowedRoles(nonSigningRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.LIST_CERTIFICATE_TYPE)
                    .allowedRoles(allowedRoles)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.FORWARD_CERTIFICATE_FROM_LIST)
                    .build(),
                CertificateActionSpecification.builder()
                    .certificateActionType(CertificateActionType.INACTIVE_CERTIFICATE_MODEL)
                    .build()))
        .elementSpecifications(mapItems(questionnaire.getItem()))
        .build();
  }

  private CertificateModelId mapModelId(Questionnaire questionnaire) {
    final var typeValue = questionnaire.getIdentifierFirstRep().getValue();
    return CertificateModelId.builder()
        .type(new CertificateType(typeValue.toLowerCase()))
        .version(new CertificateVersion(questionnaire.getVersion()))
        .build();
  }

  private CertificateTypeName mapTypeName(Questionnaire questionnaire) {
    return new CertificateTypeName(questionnaire.getTitle());
  }

  private String mapDetailedDescription(Questionnaire questionnaire) {
    return questionnaire.getUseContext().stream()
        .filter(ctx -> "purpose".equals(ctx.getCode().getCode()))
        .map(ctx -> ctx.getValueCodeableConcept().getText())
        .findFirst()
        .orElse(null);
  }

  private LocalDateTime mapActiveFrom(Questionnaire questionnaire) {
    if (!questionnaire.hasEffectivePeriod()
        || questionnaire.getEffectivePeriod().getStart() == null) {
      return null;
    }
    return questionnaire
        .getEffectivePeriod()
        .getStart()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  private List<ElementSpecification> mapItems(List<QuestionnaireItemComponent> items) {
    return items.stream().map(this::mapItem).filter(Objects::nonNull).toList();
  }

  private ElementSpecification mapItem(QuestionnaireItemComponent item) {
    return switch (item.getType()) {
      case BOOLEAN -> mapBooleanItem(item);
      default -> null;
    };
  }

  private ElementSpecification mapBooleanItem(QuestionnaireItemComponent item) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(new ElementId(linkId))
        .configuration(
            ElementConfigurationCheckboxBoolean.builder()
                .id(new FieldId(linkId))
                .label(item.getText())
                .selectedText("Ja")
                .unselectedText("Ej angivet")
                .build())
        .validations(
            List.of(ElementValidationBoolean.builder().mandatory(item.getRequired()).build()))
        .build();
  }
}