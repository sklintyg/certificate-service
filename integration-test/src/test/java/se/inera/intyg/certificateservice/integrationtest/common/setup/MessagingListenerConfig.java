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

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.connection.CachingConnectionFactory;
import se.inera.intyg.certificateservice.integrationtest.common.util.MessageListenerUtil;

@TestConfiguration
@EnableJms
@Slf4j
public class MessagingListenerConfig {

  @Bean
  public MessageListener jmsMessageSpy() {
    final var messageListener = new MessageListener();
    MessageListenerUtil.setMessageListener(messageListener);
    return messageListener;
  }

  @Bean
  DisposableBean stopJmsListenersOnShutdown(JmsListenerEndpointRegistry registry) {
    return registry::stop;
  }

  @Bean
  BeanPostProcessor disableReconnectOnExceptionForTests() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String name) {
        if (bean instanceof CachingConnectionFactory ccf) {
          ccf.setReconnectOnException(false);
        }
        return bean;
      }
    };
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory cf) {
    final var defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
    defaultJmsListenerContainerFactory.setConnectionFactory(cf);
    defaultJmsListenerContainerFactory.setPubSubDomain(false);
    defaultJmsListenerContainerFactory.setSessionTransacted(false);
    defaultJmsListenerContainerFactory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
    defaultJmsListenerContainerFactory.setConcurrency("1");
    defaultJmsListenerContainerFactory.setReceiveTimeout(100L);
    return defaultJmsListenerContainerFactory;
  }
}
