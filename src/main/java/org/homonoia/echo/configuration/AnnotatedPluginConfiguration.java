package org.homonoia.echo.configuration;

import org.homonoia.echo.bot.event.FilteringEventListenerFactory;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Configuration
@EnableConfigurationProperties(HipchatProperties.class)
public class AnnotatedPluginConfiguration {

    @Bean
    public FilteringEventListenerFactory filteringEventListenerFactory(HipchatProperties hipchatProperties) {
        return new FilteringEventListenerFactory(hipchatProperties);
    }

}
