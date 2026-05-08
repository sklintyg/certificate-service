package se.inera.intyg.certificateservice.fhir;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemType;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionFactory;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateTypeName;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateVersion;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDropdownCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.common.model.Code;
import se.inera.intyg.certificateservice.domain.common.model.Role;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCode;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCodeList;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDate;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationInteger;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationUnitContactInformation;

@Component
@RequiredArgsConstructor
public class QuestionnaireToCertificateModelMapper {

  private static final String ITEM_CONTROL_URL =
      "http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl";
  private static final String RENDERING_MARKDOWN_URL =
      "http://hl7.org/fhir/StructureDefinition/rendering-markdown";
  private static final String UNIT_OPTION_URL =
      "http://hl7.org/fhir/StructureDefinition/questionnaire-unitOption";
  private static final String CHECK_BOX_CODE = "check-box";
  private static final Pattern CATEGORY_NAME_PATTERN = Pattern.compile("###\\s+\\*\\*(.*?)\\*\\*");

  private final CertificateActionFactory certificateActionFactory;

  public CertificateModel map(Questionnaire questionnaire) {
    final var signingRoles = List.of(Role.DOCTOR, Role.PRIVATE_DOCTOR);
    final var nonSigningRoles = List.of(Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE);
    final var allowedRoles =
        Stream.concat(signingRoles.stream(), nonSigningRoles.stream()).toList();

    final var topLevelItems = questionnaire.getItem();
    final var elementSpecifications =
        IntStream.range(0, topLevelItems.size())
            .mapToObj(i -> mapTopLevelItem(topLevelItems.get(i), i + 1))
            .filter(Objects::nonNull)
            .toList();

    return CertificateModel.builder()
        .availableForCitizen(false)
        .id(mapModelId(questionnaire))
        .type(mapType(questionnaire))
        .typeName(mapTypeName(questionnaire))
        .name(questionnaire.getTitle())
        .description(questionnaire.getDescription())
        .detailedDescription(mapDetailedDescription(questionnaire))
        .activeFrom(mapActiveFrom(questionnaire))
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
        .elementSpecifications(
            Stream.concat(elementSpecifications.stream(), issuingUnitContactInfo().stream())
                .toList())
        .build();
  }

  private static List<ElementSpecification> issuingUnitContactInfo() {
    return List.of(
        ElementSpecification.builder()
            .id(UNIT_CONTACT_INFORMATION)
            .configuration(ElementConfigurationUnitContactInformation.builder().build())
            .validations(List.of(ElementValidationUnitContactInformation.builder().build()))
            .build());
  }

  private CertificateModelId mapModelId(Questionnaire questionnaire) {
    final var typeValue = questionnaire.getIdentifierFirstRep().getValue();
    return CertificateModelId.builder()
        .type(new CertificateType(typeValue.toLowerCase()))
        .version(new CertificateVersion("3.0"))
        .build();
  }

  private Code mapType(Questionnaire questionnaire) {
    final var identifier = questionnaire.getIdentifierFirstRep();
    return new Code(identifier.getValue(), identifier.getSystem(), questionnaire.getTitle());
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

  private ElementSpecification mapTopLevelItem(QuestionnaireItemComponent item, int index) {
    if (item.getType() == QuestionnaireItemType.GROUP) {
      return mapGroupItem(item);
    }
    return mapItemWithCategory(item, index);
  }

  private ElementSpecification mapItemWithCategory(QuestionnaireItemComponent item, int index) {
    final var questionSpec = mapItem(item);
    if (questionSpec == null) {
      return null;
    }
    return ElementSpecification.builder()
        .id(new ElementId("KAT_" + index))
        .configuration(
            ElementConfigurationCategory.builder().name(extractCategoryName(item)).build())
        .children(List.of(questionSpec))
        .build();
  }

  private String extractCategoryName(QuestionnaireItemComponent item) {
    for (final var ext : item.getTextElement().getExtension()) {
      if (RENDERING_MARKDOWN_URL.equals(ext.getUrl())) {
        final var markdown = ext.getValue().primitiveValue();
        if (markdown != null) {
          final var matcher = CATEGORY_NAME_PATTERN.matcher(markdown);
          if (matcher.find()) {
            return matcher.group(1).replace(":", "").strip();
          }
        }
      }
    }
    return item.getText();
  }

  private List<ElementSpecification> mapItems(List<QuestionnaireItemComponent> items) {
    return items.stream().map(this::mapItem).filter(Objects::nonNull).toList();
  }

  private ElementSpecification mapItem(QuestionnaireItemComponent item) {
    return switch (item.getType()) {
      case GROUP -> mapGroupItem(item);
      case BOOLEAN -> mapBooleanItem(item);
      case TEXT -> mapTextAreaItem(item);
      case STRING -> mapTextFieldItem(item);
      case CODING -> mapCodingItem(item);
      case DATE -> mapDateItem(item);
      case QUANTITY -> mapQuantityItem(item);
      default -> null;
    };
  }

  private ElementSpecification mapGroupItem(QuestionnaireItemComponent item) {
    return ElementSpecification.builder()
        .id(new ElementId(item.getLinkId()))
        .configuration(ElementConfigurationCategory.builder().name(item.getText()).build())
        .children(mapItems(item.getItem()))
        .build();
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
        .children(mapItems(item.getItem()))
        .build();
  }

  private ElementSpecification mapTextAreaItem(QuestionnaireItemComponent item) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(new ElementId(linkId))
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(new FieldId(linkId))
                .name(item.getText())
                .build())
        .validations(List.of(ElementValidationText.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem()))
        .build();
  }

  private ElementSpecification mapTextFieldItem(QuestionnaireItemComponent item) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(new ElementId(linkId))
        .configuration(
            ElementConfigurationTextField.builder()
                .id(new FieldId(linkId))
                .name(item.getText())
                .build())
        .validations(List.of(ElementValidationText.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem()))
        .build();
  }

  private ElementSpecification mapCodingItem(QuestionnaireItemComponent item) {
    final var linkId = item.getLinkId();
    final var options = mapAnswerOptions(item);
    final var isCheckBox = isCheckBoxControl(item);
    if (isCheckBox) {
      return ElementSpecification.builder()
          .id(new ElementId(linkId))
          .configuration(
              ElementConfigurationCheckboxMultipleCode.builder()
                  .id(new FieldId(linkId))
                  .elementLayout(ElementLayout.ROWS) // NOT COMMUNICATED FROM QUESTIONNAIRE
                  .name(item.getText())
                  .list(options)
                  .build())
          .validations(
              List.of(ElementValidationCodeList.builder().mandatory(item.getRequired()).build()))
          .children(mapItems(item.getItem()))
          .build();
    }
    return ElementSpecification.builder()
        .id(new ElementId(linkId))
        .configuration(
            ElementConfigurationDropdownCode.builder()
                .id(new FieldId(linkId))
                .name(item.getText())
                .list(options)
                .build())
        .validations(List.of(ElementValidationCode.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem()))
        .build();
  }

  private ElementSpecification mapDateItem(QuestionnaireItemComponent item) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(new ElementId(linkId))
        .configuration(
            ElementConfigurationDate.builder().id(new FieldId(linkId)).name(item.getText()).build())
        .validations(List.of(ElementValidationDate.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem()))
        .build();
  }

  private ElementSpecification mapQuantityItem(QuestionnaireItemComponent item) {
    final var linkId = item.getLinkId();
    final var unitExt = item.getExtensionByUrl(UNIT_OPTION_URL);
    final var unit = unitExt != null ? unitExt.getValue().primitiveValue() : null;
    return ElementSpecification.builder()
        .id(new ElementId(linkId))
        .configuration(
            ElementConfigurationInteger.builder()
                .id(new FieldId(linkId))
                .name(item.getText())
                .unitOfMeasurement(unit)
                .build())
        .validations(
            List.of(ElementValidationInteger.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem()))
        .build();
  }

  private boolean isCheckBoxControl(QuestionnaireItemComponent item) {
    final var ext = item.getExtensionByUrl(ITEM_CONTROL_URL);
    if (ext == null) {
      return false;
    }
    return ext.getValue() instanceof org.hl7.fhir.r5.model.CodeableConcept cc
        && CHECK_BOX_CODE.equals(cc.getCodingFirstRep().getCode());
  }

  private List<ElementConfigurationCode> mapAnswerOptions(QuestionnaireItemComponent item) {
    return item.getAnswerOption().stream()
        .map(
            option -> {
              final var coding = option.getValueCoding();
              final var code = new Code(coding.getCode(), coding.getSystem(), coding.getDisplay());
              final var fieldId = new FieldId(coding.hasId() ? coding.getId() : coding.getCode());
              return new ElementConfigurationCode(fieldId, coding.getDisplay(), code);
            })
        .toList();
  }
}