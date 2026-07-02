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
package se.inera.intyg.certificateservice.integrationtest.common.setup;

import static se.inera.intyg.certificateservice.testability.common.TestabilityConstants.TESTABILITY_PROFILE;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import se.inera.intyg.certificateservice.integrationtest.common.util.ApiUtil;
import se.inera.intyg.certificateservice.integrationtest.common.util.Containers;
import se.inera.intyg.certificateservice.integrationtest.common.util.InternalApiUtil;
import se.inera.intyg.certificateservice.integrationtest.common.util.MockServerTestUtil;
import se.inera.intyg.certificateservice.integrationtest.common.util.TestabilityApiUtil;

@ActiveProfiles({"integration-test", TESTABILITY_PROFILE})
@AutoConfigureTestRestTemplate
@SpringBootTest(
    classes = {MessagingListenerConfig.class},
    webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class ActiveCertificatesIT {

  @LocalServerPort protected int port;

  @Autowired protected TestRestTemplate restTemplate;

  protected ApiUtil api;
  protected InternalApiUtil internalApi;
  protected TestabilityApiUtil testabilityApi;

  protected BaseTestabilityUtilities baseTestabilityUtilities;

  @DynamicPropertySource
  static void testProperties(DynamicPropertyRegistry registry) {
    registry.add("certificate.model.ag114.v2_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.ag7804.v2_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk3221.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk3226.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk3226.v1_1.active.from", () -> "2026-06-01T00:00:00");
    registry.add("certificate.model.fk7210.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk7426.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk7427.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk7472.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk7804.v2_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk7809.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.fk7810.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.ts8071.v1_0.active.from", () -> "2024-01-01T00:00:00");
    registry.add("certificate.model.ts8071.v2_0.active.from", () -> "2025-10-20T00:00:00");
  }

  @BeforeAll
  static void beforeAll() {
    Containers.ensureRunning();
  }

  protected void setUpBaseIT() {
    this.api = new ApiUtil(restTemplate, port);
    this.internalApi = new InternalApiUtil(restTemplate, port);
    this.testabilityApi = new TestabilityApiUtil(restTemplate, port);
    final var mockServerClient = MockServerTestUtil.createClient();
    MockServerTestUtil.mockIntygProxyService(mockServerClient);
  }

  protected void tearDownBaseIT() {
    testabilityApi.reset();
    api.reset();
    internalApi.reset();
  }
}
