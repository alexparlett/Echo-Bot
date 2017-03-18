package org.homonoia.echo.configuration;

import org.homonoia.echo.bot.event.factory.HearListenerFactory;
import org.homonoia.echo.bot.event.factory.OnJoinListenerFactory;
import org.homonoia.echo.bot.event.factory.OnLeaveListenerFactory;
import org.homonoia.echo.bot.event.factory.RespondToListenerFactory;
import org.homonoia.echo.model.User;
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
    public HearListenerFactory hearListenerFactory(User user) {
        return new HearListenerFactory(user);
    }

    @Bean
    public RespondToListenerFactory respondToListenerFactory(User user) {
        return new RespondToListenerFactory(user);
    }

    @Bean
    public OnJoinListenerFactory onJoinListenerFactory(User user) {
        return new OnJoinListenerFactory(user);
    }

    @Bean
    public OnLeaveListenerFactory onLeaveListenerFactory(User user) {
        return new OnLeaveListenerFactory(user);
    }
}
