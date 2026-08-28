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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Writes a manifest for an already-implemented certificate, so it can be brought under {@link
 * CertificateSpecConformanceTest} without transcribing it by hand.
 *
 * <pre>
 * ./gradlew :app:test --tests '*SpecManifestBootstrapTest' -DbootstrapManifest=fk7804/v2_0
 * </pre>
 *
 * <p>The output describes what the code does today, which is not the same thing as what the
 * specification says. Diff it against the specification document before treating it as a contract —
 * that review is the entire point of the manifest.
 *
 * <p>Never use this for a certificate you are about to implement. There, the manifest comes first
 * and the code follows it.
 */
@EnabledIfSystemProperty(
    named = "bootstrapManifest",
    matches = ".+",
    disabledReason = "Run explicitly with -DbootstrapManifest=<type>/v<major>_<minor>")
class SpecManifestBootstrapTest {

  @Test
  void writeManifestFromImplementation() throws IOException {
    final var key = System.getProperty("bootstrapManifest");
    final var model = CertificateSpecConformanceTest.model(key);

    final var target =
        Paths.get(
            "src/test/resources",
            SpecManifestLoader.MANIFEST_RESOURCE_DIR,
            key,
            SpecManifestLoader.MANIFEST_FILE_NAME);
    Files.createDirectories(target.getParent());
    Files.writeString(target, SpecManifestGenerator.toYaml(model));

    System.out.printf("Wrote %s — review it against the specification document.%n", target);
  }
}
