package org.homonoia.echo.configuration;

import org.homonoia.echo.bot.event.factory.RespondToListenerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Configuration
public class AnnotatedPluginConfiguration {
    @Bean
    public RespondToListenerFactory respondToListenerFactory() {
        return new RespondToListenerFactory();
    }
}
