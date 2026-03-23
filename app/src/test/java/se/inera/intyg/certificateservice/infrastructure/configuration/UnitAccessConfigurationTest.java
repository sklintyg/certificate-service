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
package se.inera.intyg.certificateservice.infrastructure.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.certificateservice.domain.configuration.unitaccess.dto.CertificateAccessConfiguration;
import se.inera.intyg.certificateservice.domain.configuration.unitaccess.dto.CertificateAccessUnitConfiguration;

@ExtendWith(MockitoExtension.class)
class UnitAccessConfigurationTest {

  private static final String UNIT_ACCESS_WITH_VALUES =
      Paths.get("unitaccess", "unit-access-with-values.json").toString();
  private static final String INVALID_PATH = "InvalidPath";
  private static final String UNIT_ACCESS_WITH_NULL_VALUES =
      Paths.get("unitaccess", "unit-access-with-null-values.json").toString();

  private UnitAccessConfiguration unitAccessConfiguration;

  @BeforeEach
  void setUp() {
    unitAccessConfiguration = new UnitAccessConfiguration();
  }

  @Test
  void shallParseFileWithValues() {
    ReflectionTestUtils.setField(
        unitAccessConfiguration,
        "unitAccessConfigurationPath",
        new ClassPathResource(UNIT_ACCESS_WITH_VALUES));

    final var expectedCertificateAccessConfigurations =
        List.of(
            CertificateAccessConfiguration.builder()
                .certificateType("fk7809")
                .configuration(
                    List.of(
                        CertificateAccessUnitConfiguration.builder()
                            .label("Test 1")
                            .type("allow")
                            .fromDateTime(LocalDateTime.of(2024, 8, 1, 8, 0, 0))
                            .toDateTime(LocalDateTime.of(2025, 8, 1, 8, 0, 0))
                            .careProviders(List.of("TSTNMT2321000156-ALFA"))
                            .build()))
                .build(),
            CertificateAccessConfiguration.builder()
                .certificateType("fk3226")
                .configuration(
                    List.of(
                        CertificateAccessUnitConfiguration.builder()
                            .label("Test 2")
                            .type("block")
                            .fromDateTime(LocalDateTime.of(2024, 8, 1, 8, 0, 0))
                            .toDateTime(LocalDateTime.of(2025, 8, 1, 8, 0, 0))
                            .careProviders(List.of("TSTNMT2321000156-BETA"))
                            .build()))
                .build());

    final var actualCertificateAccessConfigurations = unitAccessConfiguration.get();
    assertEquals(expectedCertificateAccessConfigurations, actualCertificateAccessConfigurations);
  }

  @Test
  void shallParseFileWithNullValues() {
    ReflectionTestUtils.setField(
        unitAccessConfiguration,
        "unitAccessConfigurationPath",
        new ClassPathResource(UNIT_ACCESS_WITH_NULL_VALUES));

    final var expectedCertificateAccessConfigurations =
        List.of(
            CertificateAccessConfiguration.builder()
                .certificateType("fk7809")
                .configuration(
                    List.of(
                        CertificateAccessUnitConfiguration.builder()
                            .label("Test 1")
                            .type("allow")
                            .fromDateTime(null)
                            .toDateTime(null)
                            .careProviders(List.of("TSTNMT2321000156-ALFA"))
                            .build()))
                .build(),
            CertificateAccessConfiguration.builder()
                .certificateType("fk3226")
                .configuration(
                    List.of(
                        CertificateAccessUnitConfiguration.builder()
                            .label("Test 2")
                            .type("block")
                            .fromDateTime(null)
                            .toDateTime(null)
                            .careProviders(List.of("TSTNMT2321000156-BETA"))
                            .build()))
                .build());

    final var actualCertificateAccessConfigurations = unitAccessConfiguration.get();
    assertEquals(expectedCertificateAccessConfigurations, actualCertificateAccessConfigurations);
  }
}
