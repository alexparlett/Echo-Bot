package org.homonoia.echo.core.configuration;

import org.homonoia.echo.core.bot.event.factory.RespondToListenerFactory;
import org.homonoia.echo.core.configuration.properties.PluginsProperties;
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
@EnableConfigurationProperties(PluginsProperties.class)
public class AnnotatedPluginConfiguration {
    @Bean
    public RespondToListenerFactory respondToListenerFactory() {
        return new RespondToListenerFactory();
    }


}
