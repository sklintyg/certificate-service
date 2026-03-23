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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SnapshotTestUtil {

  private static final String SNAPSHOT_RESOURCE_DIR = "certificate-model-snapshots";

  private SnapshotTestUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static Path getSnapshotPath(String snapshotFileName) {
    final var resourceUrl =
        SnapshotTestUtil.class
            .getClassLoader()
            .getResource(SNAPSHOT_RESOURCE_DIR + "/" + snapshotFileName);

    if (resourceUrl != null) {
      try {
        return Paths.get(resourceUrl.toURI());
      } catch (URISyntaxException e) {
        throw new IllegalStateException("Failed to convert resource URL to path", e);
      }
    }

    return Paths.get("src/test/resources", SNAPSHOT_RESOURCE_DIR, snapshotFileName);
  }

  public static void generateSnapshot(String json, String snapshotFileName) throws IOException {
    final var resourceDir = Paths.get("src/test/resources", SNAPSHOT_RESOURCE_DIR);
    final var snapshotPath = resourceDir.resolve(snapshotFileName);
    Files.writeString(snapshotPath, json);
  }
}
