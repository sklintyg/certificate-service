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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRule;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.common.model.Code;

/**
 * Compares a built {@link CertificateModel} against the reviewed specification manifest.
 *
 * <p>Only elements marked {@code implemented} are checked, which is what makes the certificate
 * implementable one category per pull request: the manifest describes the whole certificate from
 * day one, while the model catches up with it increment by increment.
 *
 * <p>All findings are collected rather than thrown one at a time, so a single test run reports
 * everything that drifted.
 */
public class ModelSpecComparator {

  private ModelSpecComparator() {
    throw new IllegalStateException("Utility class");
  }

  public static List<String> compare(CertificateModel model, SpecManifest manifest) {
    final var problems = new ArrayList<String>();

    compareMetadata(model, manifest.certificate(), problems);

    final var index = new ModelIndex();
    model
        .elementSpecifications()
        .forEach(category -> index.add(category, category.id().id(), null));

    manifest.categories().forEach(category -> compareCategory(category, index, problems));

    return problems;
  }

  /** Flattened view of the model's element tree, keeping each element's category and parent. */
  private static final class ModelIndex {

    private final Map<String, ElementSpecification> elements = new HashMap<>();
    private final Map<String, String> categories = new HashMap<>();
    private final Map<String, String> parents = new HashMap<>();

    void add(ElementSpecification specification, String categoryId, String parentId) {
      final var id = specification.id().id();
      elements.put(id, specification);
      categories.put(id, categoryId);
      parents.put(id, parentId);
      specification.children().forEach(child -> add(child, categoryId, id));
    }

    ElementSpecification element(String id) {
      return elements.get(id);
    }

    String categoryOf(String id) {
      return categories.get(id);
    }

    String parentOf(String id) {
      return parents.get(id);
    }
  }

  private static void compareMetadata(
      CertificateModel model, SpecManifest.Certificate expected, List<String> problems) {
    if (expected == null) {
      problems.add("certificate: manifest has no 'certificate' section");
      return;
    }
    compare("certificate.type", expected.type(), model.id().type().type(), problems);
    compare("certificate.version", expected.version(), model.id().version().version(), problems);
    compare("certificate.name", expected.name(), model.name(), problems);
    compare("certificate.externalCode", expected.externalCode(), model.type().code(), problems);
    compare(
        "certificate.recipient",
        expected.recipient(),
        model.recipient() == null ? null : model.recipient().id().id(),
        problems);

    // Long texts are authored as YAML block scalars and emitted from Java text blocks, so line
    // breaks are an authoring artefact on both sides. Everything else is compared verbatim.
    compare(
        "certificate.description",
        withoutLineBreaks(expected.description()),
        withoutLineBreaks(model.description()),
        problems);
    compare(
        "certificate.detailedDescription",
        withoutLineBreaks(expected.detailedDescription()),
        withoutLineBreaks(model.detailedDescription()),
        problems);
  }

  private static void compareCategory(
      SpecManifest.Category category, ModelIndex index, List<String> problems) {

    final var anyImplemented =
        category.elements().stream().anyMatch(SpecManifest.Element::isImplemented);
    final var specification = index.element(category.id());

    if (specification == null) {
      if (anyImplemented) {
        problems.add(
            "%s: category is missing from the model, but %d of its elements are marked implemented"
                .formatted(category.id(), countImplemented(category)));
      }
      return;
    }

    final var configuration = specification.configuration();
    if (configuration == null || configuration.type() != ElementType.CATEGORY) {
      problems.add(
          "%s: expected an ElementConfigurationCategory but found %s"
              .formatted(category.id(), describe(configuration)));
      return;
    }
    compare(category.id() + ".name", category.name(), configuration.name(), problems);
    compare(
        category.id() + ".description",
        withoutLineBreaks(category.description()),
        withoutLineBreaks(configuration.description()),
        problems);

    category.elements().forEach(element -> compareElement(element, category, index, problems));
  }

  private static void compareElement(
      SpecManifest.Element element,
      SpecManifest.Category category,
      ModelIndex index,
      List<String> problems) {

    if (!element.isImplemented()) {
      return;
    }

    final var specification = index.element(element.id());
    if (specification == null) {
      problems.add(
          "%s: marked implemented but no element with that id exists in the model"
              .formatted(element.id()));
      return;
    }

    final var actualCategory = index.categoryOf(element.id());
    if (!Objects.equals(category.id(), actualCategory)) {
      problems.add(
          "%s: expected to sit under category %s but sits under %s"
              .formatted(element.id(), category.id(), actualCategory));
    }

    compareConfiguration(element, specification.configuration(), problems);
    compareParent(element, specification, index, problems);
    compareTextLimit(element, specification, problems);
    compareRules(element, specification, problems);
  }

  private static void compareConfiguration(
      SpecManifest.Element element, ElementConfiguration configuration, List<String> problems) {

    if (configuration == null) {
      problems.add("%s: element has no configuration".formatted(element.id()));
      return;
    }

    ComponentTypeMapping.elementType(element.component())
        .ifPresentOrElse(
            expectedType -> {
              if (configuration.type() != expectedType) {
                problems.add(
                    "%s: component %s maps to %s but the model uses %s"
                        .formatted(
                            element.id(), element.component(), expectedType, configuration.type()));
              }
            },
            () ->
                problems.add(
                    ("%s: component '%s' is not in ComponentTypeMapping — add it there and to "
                            + "certificate-elements.instructions.md")
                        .formatted(element.id(), element.component())));

    compare(element.id() + ".label", element.label(), configuration.name(), problems);
    compare(element.id() + ".helpText", element.helpText(), configuration.description(), problems);
    compare(
        element.id() + ".checkboxLabel", element.checkboxLabel(), configuration.label(), problems);
    compare(element.id() + ".header", element.header(), configuration.header(), problems);

    if (element.fieldId() != null) {
      final var actual = configuration.id();
      compare(
          element.id() + ".fieldId",
          element.fieldId(),
          actual == null ? null : actual.value(),
          problems);
    }

    compareOptions(element, configuration, problems);
  }

  private static void compareOptions(
      SpecManifest.Element element, ElementConfiguration configuration, List<String> problems) {

    if (element.options().isEmpty()) {
      return;
    }

    final var actualOptions = ConfigurationOptions.of(configuration);
    if (actualOptions.isEmpty()) {
      problems.add(
          "%s: manifest lists %d options but %s carries none"
              .formatted(element.id(), element.options().size(), describe(configuration)));
      return;
    }

    final var actualCodes = actualOptions.stream().map(Code::code).toList();
    if (!element.options().equals(actualCodes)) {
      problems.add(
          "%s: options differ%n    manifest: %s%n    model:    %s"
              .formatted(element.id(), element.options(), actualCodes));
    }

    if (element.codeSystem() != null) {
      actualOptions.stream()
          .filter(option -> !element.codeSystem().equals(option.codeSystem()))
          .findFirst()
          .ifPresent(
              option ->
                  problems.add(
                      "%s: option '%s' belongs to code system %s, manifest says %s"
                          .formatted(
                              element.id(),
                              option.code(),
                              option.codeSystem(),
                              element.codeSystem())));
    }
  }

  private static void compareParent(
      SpecManifest.Element element,
      ElementSpecification specification,
      ModelIndex index,
      List<String> problems) {

    if (element.parent() != null) {
      final var actualParent = index.parentOf(element.id());
      if (!Objects.equals(element.parent(), actualParent)) {
        problems.add(
            "%s: should be nested under %s but is nested under %s"
                .formatted(element.id(), element.parent(), describeParent(actualParent)));
      }
    }

    if (element.xmlParent() == null) {
      return;
    }

    final var mapping = specification.getMapping();
    if (mapping.isEmpty() || mapping.get().elementId() == null) {
      problems.add(
          "%s: needs an ElementMapping to %s for XML generation but has none"
              .formatted(element.id(), element.xmlParent()));
      return;
    }

    compare(
        element.id() + ".xmlParent", element.xmlParent(), mapping.get().elementId().id(), problems);
  }

  private static String describeParent(String parentId) {
    return parentId == null ? "the category directly" : parentId;
  }

  private static void compareTextLimit(
      SpecManifest.Element element, ElementSpecification specification, List<String> problems) {

    if (element.maxLength() == null) {
      return;
    }

    final var limit =
        specification.rules().stream()
            .filter(ElementRuleLimit.class::isInstance)
            .map(ElementRuleLimit.class::cast)
            .map(rule -> (int) rule.limit().value())
            .findFirst();

    if (limit.isEmpty()) {
      problems.add(
          "%s: manifest sets a limit of %d characters but the element has no TEXT_LIMIT rule"
              .formatted(element.id(), element.maxLength()));
      return;
    }

    if (!element.maxLength().equals(limit.get())) {
      problems.add(
          "%s: text limit differs — manifest %d, model %d"
              .formatted(element.id(), element.maxLength(), limit.get()));
    }
  }

  private static void compareRules(
      SpecManifest.Element element, ElementSpecification specification, List<String> problems) {

    final var actualTypes = specification.rules().stream().map(ElementRule::type).toList();

    element
        .rules()
        .forEach(
            ruleCode ->
                ComponentTypeMapping.ruleTypes(ruleCode)
                    .ifPresentOrElse(
                        expectedTypes -> {
                          if (actualTypes.stream().noneMatch(expectedTypes::contains)) {
                            problems.add(
                                "%s: rule %s expects one of %s but the element only has %s"
                                    .formatted(element.id(), ruleCode, expectedTypes, actualTypes));
                          }
                        },
                        () ->
                            problems.add(
                                ("%s: rule code '%s' is not in ComponentTypeMapping — add it there "
                                        + "and to certificate-elements.instructions.md")
                                    .formatted(element.id(), ruleCode))));
  }

  /**
   * Compares a text verbatim, except for trailing whitespace: it is invisible, carries no meaning
   * in these strings, and a YAML block scalar cannot represent it, so it is normalised away on both
   * sides. Everything else — including leading whitespace and interior line breaks — must match.
   */
  private static void compare(String path, String expected, String actual, List<String> problems) {
    final var normalizedExpected = expected == null ? null : expected.stripTrailing();
    final var normalizedActual = actual == null ? null : actual.stripTrailing();
    if (Objects.equals(normalizedExpected, normalizedActual)) {
      return;
    }
    problems.add(
        "%s: text differs%n    specification: %s%n    model:         %s"
            .formatted(path, quote(normalizedExpected), quote(normalizedActual)));
  }

  private static String quote(String value) {
    return value == null ? "<not set>" : "\"" + value + "\"";
  }

  private static String withoutLineBreaks(String value) {
    return value == null ? null : value.replaceAll("\\R", "");
  }

  private static int countImplemented(SpecManifest.Category category) {
    return (int) category.elements().stream().filter(SpecManifest.Element::isImplemented).count();
  }

  private static String describe(ElementConfiguration configuration) {
    return Optional.ofNullable(configuration)
        .map(value -> value.getClass().getSimpleName())
        .orElse("no configuration");
  }
}
