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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.CertificateModelFactoryFK7804;

/**
 * Asserts that every implemented element matches the reviewed specification manifest.
 *
 * <p>The manifest at {@code src/test/resources/certificate-specs/<type>/v<major>_<minor>/spec.yaml}
 * is the contract. If this test fails, the code is wrong — do not edit the manifest to make it
 * pass. Correcting the manifest is a separate change with a human re-reading the specification
 * document.
 *
 * <p>To cover a new certificate, add its manifest and one entry to {@link #MODELS}.
 */
class CertificateSpecConformanceTest {

  private static final Map<String, Supplier<CertificateModel>> MODELS = new LinkedHashMap<>();

  static {
    MODELS.put("fk7804/v2_0", CertificateSpecConformanceTest::fk7804);
  }

  /** Builds a registered model by manifest key, e.g. {@code fk7804/v2_0}. */
  static CertificateModel model(String key) {
    final var supplier = MODELS.get(key);
    if (supplier == null) {
      throw new IllegalArgumentException(
          "No model registered for '%s'. Registered: %s".formatted(key, MODELS.keySet()));
    }
    return supplier.get();
  }

  private static CertificateModel fk7804() {
    final var factory = new CertificateModelFactoryFK7804(null, null);
    ReflectionTestUtils.setField(factory, "activeFrom", LocalDateTime.of(2025, 8, 5, 0, 0));
    ReflectionTestUtils.setField(factory, "fkLogicalAddress", "logical-address");
    return factory.create();
  }

  private static Stream<Arguments> manifests() {
    return SpecManifestLoader.allManifests().stream().map(path -> Arguments.of(key(path), path));
  }

  @ParameterizedTest(name = "{0} matches its specification manifest")
  @MethodSource("manifests")
  void modelShouldMatchSpecification(String key, Path manifestPath) {
    final var manifest = SpecManifestLoader.load(manifestPath);

    // A manifest lands one pull request before any code does, so a manifest whose elements are all
    // still pending has nothing to check yet. As soon as an element is implemented, the model must
    // be registered.
    final var implemented =
        manifest.categories().stream()
            .flatMap(category -> category.elements().stream())
            .anyMatch(SpecManifest.Element::isImplemented);

    assumeTrue(
        implemented || MODELS.containsKey(key),
        "%s has no implemented elements yet — nothing to compare".formatted(key));

    if (!MODELS.containsKey(key)) {
      fail(
          """
          %s marks elements as implemented but no model is registered for it.
          Add an entry to CertificateSpecConformanceTest.MODELS keyed on '%s'."""
              .formatted(manifestPath, key));
    }

    final var problems = ModelSpecComparator.compare(model(key), manifest);

    assertTrue(
        problems.isEmpty(),
        () ->
            """
            The model no longer matches the reviewed specification manifest
              %s

            %d difference(s):

            %s

            The manifest is the contract. Fix the model, not the manifest — changing the manifest
            requires re-reading the specification document."""
                .formatted(manifestPath, problems.size(), String.join("\n\n", problems)));
  }

  @ParameterizedTest(name = "{0} is a well-formed manifest")
  @MethodSource("manifests")
  void manifestShouldBeWellFormed(String key, Path manifestPath) {
    final var manifest = SpecManifestLoader.load(manifestPath);

    assertTrue(manifest.certificate() != null, "%s has no 'certificate' section".formatted(key));
    assertTrue(!manifest.categories().isEmpty(), "%s declares no categories".formatted(key));

    final var unknownComponents =
        manifest.categories().stream()
            .flatMap(category -> category.elements().stream())
            .map(SpecManifest.Element::component)
            .distinct()
            .filter(component -> ComponentTypeMapping.elementType(component).isEmpty())
            .toList();

    assertTrue(
        unknownComponents.isEmpty(),
        () ->
            "%s uses component codes that ComponentTypeMapping does not know: %s"
                .formatted(key, unknownComponents));

    final var unknownRules =
        manifest.categories().stream()
            .flatMap(category -> category.elements().stream())
            .flatMap(element -> element.rules().stream())
            .distinct()
            .filter(rule -> ComponentTypeMapping.ruleTypes(rule).isEmpty())
            .toList();

    assertTrue(
        unknownRules.isEmpty(),
        () ->
            "%s uses rule codes that ComponentTypeMapping does not know: %s"
                .formatted(key, unknownRules));
  }

  /** {@code .../certificate-specs/fk7804/v2_0/spec.yaml} becomes {@code fk7804/v2_0}. */
  private static String key(Path manifestPath) {
    final var version = manifestPath.getParent();
    return version.getParent().getFileName() + "/" + version.getFileName();
  }
}
