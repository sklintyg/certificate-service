package se.inera.intyg.certificateservice.fhir;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemEnableWhenComponent;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemOperator;
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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRule;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleLimit;
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

  /**
   * Creates a FieldId from a FHIR linkId, replacing hyphens with underscores so the value is a
   * valid identifier in the client expression language.
   */
  private static FieldId toFieldId(String linkId) {
    return new FieldId(linkId.replace("-", "_"));
  }

  /**
   * Creates an ElementId from a FHIR linkId, replacing hyphens with underscores so the id matches
   * the corresponding FieldId and expression references.
   */
  private static ElementId toElementId(String linkId) {
    return new ElementId(linkId.replace("-", "_"));
  }

  public CertificateModel map(Questionnaire questionnaire) {
    final var signingRoles = List.of(Role.DOCTOR, Role.PRIVATE_DOCTOR);
    final var nonSigningRoles = List.of(Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE);
    final var allowedRoles =
        Stream.concat(signingRoles.stream(), nonSigningRoles.stream()).toList();

    final var topLevelItems = questionnaire.getItem();
    final var itemIndex = buildItemIndex(topLevelItems);
    final var elementSpecifications =
        IntStream.range(0, topLevelItems.size())
            .mapToObj(i -> mapTopLevelItem(topLevelItems.get(i), i + 1, itemIndex))
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

  private ElementSpecification mapTopLevelItem(
      QuestionnaireItemComponent item,
      int categoryIndex,
      Map<String, QuestionnaireItemComponent> itemIndex) {
    if (item.getType() == QuestionnaireItemType.GROUP) {
      return mapGroupItem(item, itemIndex);
    }
    return mapItemWithCategory(item, categoryIndex, itemIndex);
  }

  private ElementSpecification mapItemWithCategory(
      QuestionnaireItemComponent item,
      int categoryIndex,
      Map<String, QuestionnaireItemComponent> itemIndex) {
    final var questionSpec = mapItem(item, itemIndex);
    if (questionSpec == null) {
      return null;
    }
    return ElementSpecification.builder()
        .id(new ElementId("KAT_" + categoryIndex))
        .configuration(
            ElementConfigurationCategory.builder().name(extractCategoryName(item)).build())
        .rules(mapCategoryRulesFromItem(item, itemIndex))
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

  private List<ElementSpecification> mapItems(
      List<QuestionnaireItemComponent> items,
      Map<String, QuestionnaireItemComponent> itemIndex) {
    return items.stream().map(item -> mapItem(item, itemIndex)).filter(Objects::nonNull).toList();
  }

  private ElementSpecification mapItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    return switch (item.getType()) {
      case GROUP -> mapGroupItem(item, itemIndex);
      case BOOLEAN -> mapBooleanItem(item, itemIndex);
      case TEXT -> mapTextAreaItem(item, itemIndex);
      case STRING -> mapTextFieldItem(item, itemIndex);
      case CODING -> mapCodingItem(item, itemIndex);
      case DATE -> mapDateItem(item, itemIndex);
      case QUANTITY -> mapQuantityItem(item, itemIndex);
      default -> null;
    };
  }

  private ElementSpecification mapGroupItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    return ElementSpecification.builder()
        .id(toElementId(item.getLinkId()))
        .configuration(ElementConfigurationCategory.builder().name(item.getText()).build())
        .children(mapItems(item.getItem(), itemIndex))
        .build();
  }

  private ElementSpecification mapBooleanItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(toElementId(linkId))
        .configuration(
            ElementConfigurationCheckboxBoolean.builder()
                .id(toFieldId(linkId))
                .label(item.getText())
                .selectedText("Ja")
                .unselectedText("Ej angivet")
                .build())
        .rules(mapRulesFromItem(item, itemIndex))
        .validations(
            List.of(ElementValidationBoolean.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem(), itemIndex))
        .build();
  }

  private ElementSpecification mapTextAreaItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(toElementId(linkId))
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(toFieldId(linkId))
                .name(item.getText())
                .build())
        .rules(mapRulesFromItem(item, itemIndex))
        .validations(List.of(ElementValidationText.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem(), itemIndex))
        .build();
  }

  private ElementSpecification mapTextFieldItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(toElementId(linkId))
        .configuration(
            ElementConfigurationTextField.builder()
                .id(toFieldId(linkId))
                .name(item.getText())
                .build())
        .rules(mapRulesFromItem(item, itemIndex))
        .validations(List.of(ElementValidationText.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem(), itemIndex))
        .build();
  }

  private ElementSpecification mapCodingItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var linkId = item.getLinkId();
    final var options = mapAnswerOptions(item);
    final var isCheckBox = isCheckBoxControl(item);
    if (isCheckBox) {
      return ElementSpecification.builder()
          .id(toElementId(linkId))
          .configuration(
              ElementConfigurationCheckboxMultipleCode.builder()
                  .id(toFieldId(linkId))
                  .elementLayout(ElementLayout.ROWS) // NOT COMMUNICATED FROM QUESTIONNAIRE
                  .name(item.getText())
                  .list(options)
                  .build())
          .rules(mapRulesFromItem(item, itemIndex))
          .validations(
              List.of(ElementValidationCodeList.builder().mandatory(item.getRequired()).build()))
          .children(mapItems(item.getItem(), itemIndex))
          .build();
    }
    return ElementSpecification.builder()
        .id(toElementId(linkId))
        .configuration(
            ElementConfigurationDropdownCode.builder()
                .id(toFieldId(linkId))
                .name(item.getText())
                .list(options)
                .build())
        .rules(mapRulesFromItem(item, itemIndex))
        .validations(List.of(ElementValidationCode.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem(), itemIndex))
        .build();
  }

  private ElementSpecification mapDateItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var linkId = item.getLinkId();
    return ElementSpecification.builder()
        .id(toElementId(linkId))
        .configuration(
            ElementConfigurationDate.builder()
                .id(toFieldId(linkId))
                .name(item.getText())
                .build())
        .rules(mapRulesFromItem(item, itemIndex))
        .validations(List.of(ElementValidationDate.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem(), itemIndex))
        .build();
  }

  private ElementSpecification mapQuantityItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var linkId = item.getLinkId();
    final var unitExt = item.getExtensionByUrl(UNIT_OPTION_URL);
    final var unit = unitExt != null ? unitExt.getValue().primitiveValue() : null;
    return ElementSpecification.builder()
        .id(toElementId(linkId))
        .configuration(
            ElementConfigurationInteger.builder()
                .id(toFieldId(linkId))
                .name(item.getText())
                .unitOfMeasurement(unit)
                .build())
        .rules(mapRulesFromItem(item, itemIndex))
        .validations(
            List.of(ElementValidationInteger.builder().mandatory(item.getRequired()).build()))
        .children(mapItems(item.getItem(), itemIndex))
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
              final var fieldId = toFieldId(coding.hasId() ? coding.getId() : coding.getCode());
              return new ElementConfigurationCode(fieldId, coding.getDisplay(), code);
            })
        .toList();
  }

  private Map<String, QuestionnaireItemComponent> buildItemIndex(
      List<QuestionnaireItemComponent> items) {
    final var index = new HashMap<String, QuestionnaireItemComponent>();
    indexItems(items, index);
    return index;
  }

  private void indexItems(
      List<QuestionnaireItemComponent> items,
      Map<String, QuestionnaireItemComponent> index) {
    for (final var item : items) {
      index.put(item.getLinkId(), item);
      if (!item.getItem().isEmpty()) {
        indexItems(item.getItem(), index);
      }
    }
  }

  private List<ElementRule> mapRulesFromItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    final var rules = new ArrayList<ElementRule>();

    for (final var enableWhen : item.getEnableWhen()) {
      if (enableWhen.getOperator() != QuestionnaireItemOperator.EQUAL) {
        continue;
      }
      final var parentLinkId = enableWhen.getQuestion();
      final var parentItem = itemIndex.get(parentLinkId);
      if (parentItem == null) {
        continue;
      }
      final var parentId = toElementId(parentLinkId);
      if (enableWhen.hasAnswerBooleanType()) {
        final var fieldId = toFieldId(parentLinkId);
        rules.add(
            enableWhen.getAnswerBooleanType().booleanValue()
                ? buildShowRule(parentId, fieldId)
                : buildHideRule(parentId, fieldId));
      } else if (enableWhen.hasAnswerCoding()) {
        final var code = enableWhen.getAnswerCoding().getCode();
        final var optionFieldId = lookupOptionFieldId(parentItem, code);
        if (optionFieldId != null) {
          rules.add(buildShowRule(parentId, optionFieldId));
        }
      }
    }

    if (item.getRequired()) {
      final var id = toElementId(item.getLinkId());
      rules.add(buildMandatoryRule(item, id));
    }

    if (item.hasMaxLength() && item.getMaxLength() > 0) {
      rules.add(buildLimitRule(toElementId(item.getLinkId()), (short) item.getMaxLength()));
    }

    return rules;
  }

  private List<ElementRule> mapCategoryRulesFromItem(
      QuestionnaireItemComponent item, Map<String, QuestionnaireItemComponent> itemIndex) {
    return item.getEnableWhen().stream()
        .filter(
            enableWhen ->
                enableWhen.getOperator() == QuestionnaireItemOperator.EQUAL
                    && enableWhen.hasAnswerBooleanType())
        .map(
            enableWhen -> {
              final var parentLinkId = enableWhen.getQuestion();
              final var parentId = toElementId(parentLinkId);
              final var fieldId = toFieldId(parentLinkId);
              return enableWhen.getAnswerBooleanType().booleanValue()
                  ? buildShowRule(parentId, fieldId)
                  : buildHideRule(parentId, fieldId);
            })
        .collect(Collectors.toList());
  }

  private ElementRule buildMandatoryRule(QuestionnaireItemComponent item, ElementId id) {
    final var fieldId = toFieldId(item.getLinkId()).value();
    if (item.getType() == QuestionnaireItemType.BOOLEAN) {
      return ElementRuleExpression.builder()
          .id(id)
          .type(ElementRuleType.MANDATORY)
          .expression(new RuleExpression("exists($" + fieldId + ")"))
          .build();
    }
    if (item.getType() == QuestionnaireItemType.CODING && isCheckBoxControl(item)) {
      final var optionExpressions =
          mapAnswerOptions(item).stream()
              .map(opt -> "$" + opt.id().value())
              .collect(Collectors.joining(" || "));
      return ElementRuleExpression.builder()
          .id(id)
          .type(ElementRuleType.MANDATORY)
          .expression(new RuleExpression(optionExpressions))
          .build();
    }
    return ElementRuleExpression.builder()
        .id(id)
        .type(ElementRuleType.MANDATORY)
        .expression(new RuleExpression("$" + fieldId))
        .build();
  }

  private static ElementRule buildShowRule(ElementId id, FieldId fieldId) {
    return ElementRuleExpression.builder()
        .type(ElementRuleType.SHOW)
        .id(id)
        .expression(new RuleExpression("$" + fieldId.value()))
        .build();
  }

  private static ElementRule buildHideRule(ElementId id, FieldId fieldId) {
    return ElementRuleExpression.builder()
        .type(ElementRuleType.HIDE)
        .id(id)
        .expression(new RuleExpression("$" + fieldId.value()))
        .build();
  }

  private static ElementRule buildLimitRule(ElementId id, short limit) {
    return ElementRuleLimit.builder()
        .id(id)
        .type(ElementRuleType.TEXT_LIMIT)
        .limit(new RuleLimit(limit))
        .build();
  }

  private FieldId lookupOptionFieldId(QuestionnaireItemComponent parent, String code) {
    return parent.getAnswerOption().stream()
        .filter(opt -> code.equals(opt.getValueCoding().getCode()))
        .map(
            opt -> {
              final var coding = opt.getValueCoding();
              return toFieldId(coding.hasId() ? coding.getId() : coding.getCode());
            })
        .findFirst()
        .orElse(null);
  }
}