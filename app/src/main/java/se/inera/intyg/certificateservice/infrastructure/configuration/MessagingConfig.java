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

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import java.util.List;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class MessagingConfig {

  @Value("${certificate.event.queue.name}")
  private String eventQueueName;

  @Value("${spring.activemq.broker-url}")
  private String brokerUrl;

  @Value("${spring.activemq.user}")
  private String amqUser;

  @Value("${spring.activemq.password}")
  private String amqPassword;

  @Bean
  public Queue eventQueue() {
    return new ActiveMQQueue(eventQueueName);
  }

  @Bean
  public ConnectionFactory cachingConnectionFactory() {
    final var activeMqConnectionFactory =
        new ActiveMQConnectionFactory(amqUser, amqPassword, brokerUrl);
    activeMqConnectionFactory.setTrustedPackages(
        List.of("se.inera.intyg.certificateservice.infrastructure.messaging"));
    return new CachingConnectionFactory(activeMqConnectionFactory);
  }

  @Bean
  public JmsTemplate eventJmsTemplate() {
    final var jmsTemplate = new JmsTemplate(cachingConnectionFactory());
    jmsTemplate.setDefaultDestination(eventQueue());
    jmsTemplate.setSessionTransacted(true);
    return jmsTemplate;
  }
}
