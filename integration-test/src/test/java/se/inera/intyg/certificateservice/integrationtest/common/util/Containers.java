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
package se.inera.intyg.certificateservice.integrationtest.common.util;

import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class Containers {

  public static ActiveMQContainer AMQ_CONTAINER;
  public static MockServerContainer MOCK_SERVER_CONTAINER;

  public static void ensureRunning() {
    amqContainer();
    mockServerContainer();
  }

  private static void amqContainer() {
    if (AMQ_CONTAINER == null) {
      AMQ_CONTAINER =
          new ActiveMQContainer("apache/activemq-classic:5.18.3")
              .withUser("activemqUser")
              .withPassword("activemqPassword");
    }

    if (!AMQ_CONTAINER.isRunning()) {
      AMQ_CONTAINER.start();
    }

    System.setProperty("spring.activemq.user", AMQ_CONTAINER.getUser());
    System.setProperty("spring.activemq.password", AMQ_CONTAINER.getPassword());
    System.setProperty("spring.activemq.broker-url", AMQ_CONTAINER.getBrokerUrl());
  }

  private static void mockServerContainer() {
    if (MOCK_SERVER_CONTAINER == null) {
      MOCK_SERVER_CONTAINER =
          new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));
    }

    if (!MOCK_SERVER_CONTAINER.isRunning()) {
      MOCK_SERVER_CONTAINER.start();
    }

    final var mockServerContainerHost = MOCK_SERVER_CONTAINER.getHost();
    final var mockServerContainerPort = String.valueOf(MOCK_SERVER_CONTAINER.getServerPort());

    System.setProperty(
        "integration.certificateprintservice.address",
        String.format("http://%s:%s", mockServerContainerHost, mockServerContainerPort));

    System.setProperty(
        "integration.intygproxyservice.address",
        String.format("http://%s:%s", mockServerContainerHost, mockServerContainerPort));
  }
}
