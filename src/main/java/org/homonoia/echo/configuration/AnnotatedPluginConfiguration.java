package org.homonoia.echo.configuration;

import org.homonoia.echo.bot.event.factory.HearListenerFactory;
import org.homonoia.echo.bot.event.factory.OnJoinListenerFactory;
import org.homonoia.echo.bot.event.factory.OnLeaveListenerFactory;
import org.homonoia.echo.bot.event.factory.RespondToListenerFactory;
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
    public HearListenerFactory hearListenerFactory(HipchatProperties hipchatProperties) {
        return new HearListenerFactory(hipchatProperties);
    }

    @Bean
    public RespondToListenerFactory respondToListenerFactory(HipchatProperties hipchatProperties) {
        return new RespondToListenerFactory(hipchatProperties);
    }

    @Bean
    public OnJoinListenerFactory onJoinListenerFactory(HipchatProperties hipchatProperties) {
        return new OnJoinListenerFactory(hipchatProperties);
    }

    @Bean
    public OnLeaveListenerFactory onLeaveListenerFactory(HipchatProperties hipchatProperties) {
        return new OnLeaveListenerFactory(hipchatProperties);
    }

}
