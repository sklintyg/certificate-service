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
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.certificateservice.domain.configuration.limitedcertificatefunctionality.dto.LimitedActionConfiguration;
import se.inera.intyg.certificateservice.domain.configuration.limitedcertificatefunctionality.dto.LimitedCertificateFunctionalityActionsConfiguration;
import se.inera.intyg.certificateservice.domain.configuration.limitedcertificatefunctionality.dto.LimitedCertificateFunctionalityConfiguration;

@ExtendWith(MockitoExtension.class)
class GetLimitedCertificateFunctionalityConfigurationTest {

  private static final String LIMITED_FUNCTIONALITY_WITH_SEND_CONFIG =
      Paths.get("limitedfunctionality", "limited-functionality-with-send-config.json").toString();

  private static final String LIMITED_FUNCTIONALITY_WITHOUT_ACTIONS =
      Paths.get("limitedfunctionality", "limited-functionality-without-actions.json").toString();

  private GetLimitedCertificateFunctionalityConfiguration
      getLimitedCertificateFunctionalityConfiguration;

  @BeforeEach
  void setUp() {
    getLimitedCertificateFunctionalityConfiguration =
        new GetLimitedCertificateFunctionalityConfiguration();
  }

  @Test
  void shallParseFileWithActionSend() {
    ReflectionTestUtils.setField(
        getLimitedCertificateFunctionalityConfiguration,
        "limitedCertificateFunctionalityConfigurationPath",
        new ClassPathResource(LIMITED_FUNCTIONALITY_WITH_SEND_CONFIG));

    final var expectedInactiveCertificateConfiguration =
        List.of(
            LimitedCertificateFunctionalityConfiguration.builder()
                .certificateType("ts8071")
                .version(List.of("1.0"))
                .configuration(
                    LimitedCertificateFunctionalityActionsConfiguration.builder()
                        .actions(
                            List.of(
                                LimitedActionConfiguration.builder()
                                    .type("SEND")
                                    .untilDateTime(LocalDateTime.of(2024, 8, 1, 8, 0, 0))
                                    .build()))
                        .build())
                .build());

    final var actualCertificateAccessConfigurations =
        getLimitedCertificateFunctionalityConfiguration.get();
    assertEquals(expectedInactiveCertificateConfiguration, actualCertificateAccessConfigurations);
  }

  @Test
  void shallParseFileWithoutActions() {
    ReflectionTestUtils.setField(
        getLimitedCertificateFunctionalityConfiguration,
        "limitedCertificateFunctionalityConfigurationPath",
        new ClassPathResource(LIMITED_FUNCTIONALITY_WITHOUT_ACTIONS));

    final var expectedInactiveCertificateConfiguration =
        List.of(
            LimitedCertificateFunctionalityConfiguration.builder()
                .certificateType("ts8071")
                .version(List.of("1.0"))
                .configuration(
                    LimitedCertificateFunctionalityActionsConfiguration.builder()
                        .actions(Collections.emptyList())
                        .build())
                .build());

    final var actualCertificateAccessConfigurations =
        getLimitedCertificateFunctionalityConfiguration.get();
    assertEquals(expectedInactiveCertificateConfiguration, actualCertificateAccessConfigurations);
  }
}
