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

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

/**
 * Maps the codes used by the specification documents onto domain types.
 *
 * <p>This is the executable copy of the tables in {@code
 * .github/instructions/certificate-elements.instructions.md}. Keep the two in step: an unmapped
 * code fails the conformance test rather than being ignored.
 */
public class ComponentTypeMapping {

  private static final Map<String, ElementType> COMPONENTS =
      Map.ofEntries(
          Map.entry("sk-000", ElementType.CATEGORY),
          Map.entry("sk-001", ElementType.CHECKBOX_BOOLEAN),
          Map.entry("sk-002", ElementType.RADIO_BOOLEAN),
          Map.entry("sk-002(boolesk)", ElementType.RADIO_BOOLEAN),
          Map.entry("sk-002(kodverk)", ElementType.RADIO_MULTIPLE_CODE),
          Map.entry("sk-002(kod)", ElementType.RADIO_MULTIPLE_CODE),
          Map.entry("sk-004", ElementType.CHECKBOX_MULTIPLE_CODE),
          Map.entry("sk-004(kodverk)", ElementType.CHECKBOX_MULTIPLE_CODE),
          Map.entry("sk-004(koder)", ElementType.CHECKBOX_MULTIPLE_CODE),
          Map.entry("sk-004a(datum)", ElementType.CHECKBOX_MULTIPLE_DATE),
          Map.entry("sk-004a(kodermeddatum)", ElementType.CHECKBOX_MULTIPLE_DATE),
          Map.entry("sk-004a(datumperiod)", ElementType.CHECKBOX_DATE_RANGE_LIST),
          Map.entry("sk-004a(kodermeddatumperioder)", ElementType.CHECKBOX_DATE_RANGE_LIST),
          Map.entry("sk-005(datum)", ElementType.DATE),
          Map.entry("sk-005(datumperiod)", ElementType.DATE_RANGE),
          Map.entry("sk-006", ElementType.TEXT_FIELD),
          Map.entry("sk-006(text)", ElementType.TEXT_FIELD),
          Map.entry("sk-007", ElementType.TEXT_AREA),
          Map.entry("sk-007(text)", ElementType.TEXT_AREA),
          Map.entry("sk-007(icf)", ElementType.ICF),
          Map.entry("sk-008", ElementType.DROPDOWN),
          Map.entry("sk-008(kodverk)", ElementType.DROPDOWN),
          Map.entry("sk-009", ElementType.INTEGER),
          Map.entry("sk-009(numeriskt)", ElementType.INTEGER),
          Map.entry("sk-a01", ElementType.MESSAGE),
          Map.entry("sk-a02", ElementType.MESSAGE),
          Map.entry("sk-a03", ElementType.MESSAGE),
          Map.entry("sk-a04", ElementType.MESSAGE),
          Map.entry("(diagnoser)", ElementType.DIAGNOSIS),
          Map.entry("(utredning)", ElementType.MEDICAL_INVESTIGATION_LIST),
          Map.entry("(synskarpa)", ElementType.VISUAL_ACUITIES),
          Map.entry("(vardenhet)", ElementType.ISSUING_UNIT));

  /**
   * A specification rule code may legitimately be realised as one of several rule types, so each
   * code maps to the set that satisfies it.
   */
  private static final Map<String, Set<ElementRuleType>> RULES =
      Map.of(
          "sr-001", Set.of(ElementRuleType.MANDATORY),
          "sr-002", Set.of(ElementRuleType.CATEGORY_MANDATORY),
          "sr-003", Set.of(ElementRuleType.SHOW),
          "sr-004", Set.of(ElementRuleType.HIDE),
          "sr-005", Set.of(ElementRuleType.DISABLE, ElementRuleType.DISABLE_SUB_ELEMENT),
          "sr-006", Set.of(ElementRuleType.AUTO_FILL),
          "sr-008", Set.of(ElementRuleType.DISABLE, ElementRuleType.DISABLE_SUB_ELEMENT));

  private static final Map<ElementType, String> CANONICAL_COMPONENTS =
      Map.ofEntries(
          Map.entry(ElementType.CATEGORY, "SK-000"),
          Map.entry(ElementType.CHECKBOX_BOOLEAN, "SK-001"),
          Map.entry(ElementType.RADIO_BOOLEAN, "SK-002 (boolesk)"),
          Map.entry(ElementType.RADIO_MULTIPLE_CODE, "SK-002 (kodverk)"),
          Map.entry(ElementType.CHECKBOX_MULTIPLE_CODE, "SK-004 (kodverk)"),
          Map.entry(ElementType.CHECKBOX_MULTIPLE_DATE, "SK-004a (datum)"),
          Map.entry(ElementType.CHECKBOX_DATE_RANGE_LIST, "SK-004a (datumperiod)"),
          Map.entry(ElementType.DATE, "SK-005 (datum)"),
          Map.entry(ElementType.DATE_RANGE, "SK-005 (datumperiod)"),
          Map.entry(ElementType.TEXT_FIELD, "SK-006"),
          Map.entry(ElementType.TEXT_AREA, "SK-007"),
          Map.entry(ElementType.ICF, "SK-007 (icf)"),
          Map.entry(ElementType.DROPDOWN, "SK-008"),
          Map.entry(ElementType.INTEGER, "SK-009"),
          Map.entry(ElementType.MESSAGE, "SK-A01"),
          Map.entry(ElementType.DIAGNOSIS, "(diagnoser)"),
          Map.entry(ElementType.MEDICAL_INVESTIGATION_LIST, "(utredning)"),
          Map.entry(ElementType.VISUAL_ACUITIES, "(synskarpa)"),
          Map.entry(ElementType.ISSUING_UNIT, "(vardenhet)"));

  private static final Map<ElementRuleType, String> CANONICAL_RULES =
      Map.of(
          ElementRuleType.MANDATORY, "SR-001",
          ElementRuleType.CATEGORY_MANDATORY, "SR-002",
          ElementRuleType.SHOW, "SR-003",
          ElementRuleType.HIDE, "SR-004",
          ElementRuleType.DISABLE, "SR-005",
          ElementRuleType.DISABLE_SUB_ELEMENT, "SR-005",
          ElementRuleType.AUTO_FILL, "SR-006");

  private ComponentTypeMapping() {
    throw new IllegalStateException("Utility class");
  }

  public static Optional<ElementType> elementType(String component) {
    return Optional.ofNullable(COMPONENTS.get(normalize(component)));
  }

  /** The component code a {@link SpecManifest} should use for a given configuration type. */
  public static Optional<String> component(ElementType elementType) {
    return Optional.ofNullable(CANONICAL_COMPONENTS.get(elementType));
  }

  /** The specification rule code a given rule type realises, if it has one. */
  public static Optional<String> ruleCode(ElementRuleType ruleType) {
    return Optional.ofNullable(CANONICAL_RULES.get(ruleType));
  }

  public static Optional<Set<ElementRuleType>> ruleTypes(String ruleCode) {
    return Optional.ofNullable(RULES.get(normalize(ruleCode)));
  }

  /**
   * Codes are compared case-insensitively and ignoring whitespace and the diacritics the
   * specification uses inconsistently, so both {@code SK-004a (koder med datum)} and {@code
   * SK-004A(KoderMedDatum)} resolve.
   */
  private static String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value
        .toLowerCase(Locale.ROOT)
        .replace("ä", "a")
        .replace("å", "a")
        .replace("ö", "o")
        .replaceAll("\\s+", "");
  }
}
