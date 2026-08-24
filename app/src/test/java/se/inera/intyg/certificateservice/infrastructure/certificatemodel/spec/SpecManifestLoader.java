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
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.dataformat.yaml.YAMLMapper;

/** Loads and locates certificate specification manifests. */
public class SpecManifestLoader {

  public static final String MANIFEST_RESOURCE_DIR = "certificate-specs";
  public static final String MANIFEST_FILE_NAME = "spec.yaml";

  private static final YAMLMapper MAPPER =
      YAMLMapper.builder()
          .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
          .build();

  private SpecManifestLoader() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Reads a manifest. Unknown properties fail the read, so a typo in a manifest surfaces as a test
   * failure rather than as a silently ignored field.
   */
  public static SpecManifest load(Path path) {
    try {
      return MAPPER.readValue(Files.readString(path), SpecManifest.class);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read spec manifest: " + path, e);
    }
  }

  /** Resolves {@code certificate-specs/<type>/v<major>_<minor>/spec.yaml}. */
  public static Path manifestPath(String type, String version) {
    return manifestDir().resolve(type).resolve(versionDir(version)).resolve(MANIFEST_FILE_NAME);
  }

  /** Every manifest on the test classpath, ordered by path for stable test naming. */
  public static List<Path> allManifests() {
    final var root = manifestDir();
    if (!Files.isDirectory(root)) {
      return List.of();
    }
    try (var paths = Files.walk(root)) {
      return paths
          .filter(path -> path.getFileName().toString().equals(MANIFEST_FILE_NAME))
          .sorted(Comparator.comparing(Path::toString))
          .toList();
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to list spec manifests below " + root, e);
    }
  }

  private static String versionDir(String version) {
    return "v" + version.replace('.', '_');
  }

  private static Path manifestDir() {
    final var resourceUrl =
        SpecManifestLoader.class.getClassLoader().getResource(MANIFEST_RESOURCE_DIR);

    if (resourceUrl != null) {
      try {
        return Paths.get(resourceUrl.toURI());
      } catch (URISyntaxException e) {
        throw new IllegalStateException("Failed to convert resource URL to path", e);
      }
    }

    return Paths.get("src/test/resources", MANIFEST_RESOURCE_DIR);
  }
}
