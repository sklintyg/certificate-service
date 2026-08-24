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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.spec;

import java.util.List;
import java.util.Optional;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.common.model.Code;

/**
 * Renders a {@link CertificateModel} as a specification manifest.
 *
 * <p>This exists to bootstrap manifests for certificates that were implemented before manifests
 * existed, and to let {@code cert-verify} state what the code currently says in the same vocabulary
 * as the specification. It is <strong>not</strong> how a new certificate's manifest is produced —
 * that one is written from the specification document and reviewed against it, otherwise the
 * conformance test would only prove that the code equals itself.
 */
public class SpecManifestGenerator {

  private SpecManifestGenerator() {
    throw new IllegalStateException("Utility class");
  }

  public static String toYaml(CertificateModel model) {
    final var yaml = new StringBuilder();
    yaml.append("# Generated from the implementation by SpecManifestGenerator.\n")
        .append("# Review every text against the specification document before trusting it.\n\n");

    yaml.append("certificate:\n");
    scalar(yaml, 1, "type", model.id().type().type());
    scalar(yaml, 1, "version", model.id().version().version());
    scalar(yaml, 1, "externalCode", model.type() == null ? null : model.type().code());
    scalar(yaml, 1, "name", model.name());
    scalar(yaml, 1, "recipient", model.recipient() == null ? null : model.recipient().id().id());
    scalar(yaml, 1, "description", model.description());
    scalar(yaml, 1, "detailedDescription", model.detailedDescription());

    yaml.append("\ncategories:\n");
    model.elementSpecifications().stream()
        .filter(element -> !isIssuingUnit(element))
        .forEach(category -> appendCategory(yaml, category));
    return yaml.toString();
  }

  private static void appendCategory(StringBuilder yaml, ElementSpecification category) {
    final var configuration = category.configuration();
    yaml.append("  - id: ").append(quote(category.id().id())).append('\n');
    scalar(yaml, 2, "name", configuration == null ? null : configuration.name());
    scalar(yaml, 2, "description", configuration == null ? null : configuration.description());

    if (category.children().isEmpty()) {
      return;
    }
    yaml.append("    elements:\n");
    category.children().forEach(child -> appendElement(yaml, child, category.id().id(), null));
  }

  private static void appendElement(
      StringBuilder yaml, ElementSpecification element, String categoryId, String parentId) {

    final var configuration = element.configuration();
    yaml.append("      - id: ").append(quote(element.id().id())).append('\n');
    scalar(yaml, 4, "kind", parentId == null ? "FRG" : "DFR");
    scalar(yaml, 4, "component", component(configuration));
    scalar(yaml, 4, "label", configuration == null ? null : configuration.name());
    scalar(yaml, 4, "helpText", configuration == null ? null : configuration.description());
    scalar(yaml, 4, "checkboxLabel", configuration == null ? null : configuration.label());
    scalar(yaml, 4, "header", configuration == null ? null : configuration.header());
    scalar(yaml, 4, "parent", parentId);
    scalar(yaml, 4, "xmlParent", xmlParent(element));
    scalar(
        yaml,
        4,
        "fieldId",
        configuration == null || configuration.id() == null ? null : configuration.id().value());
    integer(yaml, 4, "maxLength", maxLength(element));
    scalar(yaml, 4, "codeSystem", codeSystem(configuration));
    list(
        yaml,
        4,
        "options",
        ConfigurationOptions.of(configuration).stream().map(Code::code).toList());
    list(yaml, 4, "rules", ruleCodes(element));
    scalar(yaml, 4, "status", SpecManifest.Element.STATUS_IMPLEMENTED);

    element.children().forEach(child -> appendElement(yaml, child, categoryId, element.id().id()));
  }

  private static String xmlParent(ElementSpecification element) {
    return element
        .getMapping()
        .map(mapping -> mapping.elementId() == null ? null : mapping.elementId().id())
        .orElse(null);
  }

  private static boolean isIssuingUnit(ElementSpecification element) {
    final var configuration = element.configuration();
    return configuration != null
        && configuration.type()
            == se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType
                .ISSUING_UNIT;
  }

  private static String component(ElementConfiguration configuration) {
    if (configuration == null) {
      return null;
    }
    return ComponentTypeMapping.component(configuration.type())
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "No manifest component code for ElementType " + configuration.type()));
  }

  private static Integer maxLength(ElementSpecification element) {
    return element.rules().stream()
        .filter(ElementRuleLimit.class::isInstance)
        .map(ElementRuleLimit.class::cast)
        .map(rule -> (int) rule.limit().value())
        .findFirst()
        .orElse(null);
  }

  private static List<String> ruleCodes(ElementSpecification element) {
    return element.rules().stream()
        .map(rule -> rule.type() == ElementRuleType.TEXT_LIMIT ? null : rule.type())
        .filter(java.util.Objects::nonNull)
        .map(ComponentTypeMapping::ruleCode)
        .flatMap(Optional::stream)
        .distinct()
        .toList();
  }

  private static String codeSystem(ElementConfiguration configuration) {
    return ConfigurationOptions.of(configuration).stream()
        .findFirst()
        .map(Code::codeSystem)
        .orElse(null);
  }

  private static void scalar(StringBuilder yaml, int indentLevel, String key, String value) {
    if (value == null || value.isEmpty()) {
      return;
    }
    final var indent = "  ".repeat(indentLevel);
    if (value.contains("\n")) {
      yaml.append(indent).append(key).append(": |-\n");
      value.lines().forEach(line -> yaml.append(indent).append("  ").append(line).append('\n'));
      return;
    }
    yaml.append(indent).append(key).append(": ").append(quote(value)).append('\n');
  }

  private static void integer(StringBuilder yaml, int indentLevel, String key, Integer value) {
    if (value == null) {
      return;
    }
    yaml.append("  ".repeat(indentLevel)).append(key).append(": ").append(value).append('\n');
  }

  private static void list(StringBuilder yaml, int indentLevel, String key, List<String> values) {
    if (values.isEmpty()) {
      return;
    }
    yaml.append("  ".repeat(indentLevel)).append(key).append(": [");
    yaml.append(String.join(", ", values.stream().map(SpecManifestGenerator::quote).toList()));
    yaml.append("]\n");
  }

  private static String quote(String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
  }
}
