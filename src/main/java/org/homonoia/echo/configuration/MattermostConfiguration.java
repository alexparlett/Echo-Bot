package org.homonoia.echo.configuration;

import net.bis5.mattermost.client4.MattermostClient;
import org.homonoia.echo.configuration.properties.MattermostProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Configuration
@EnableConfigurationProperties(MattermostProperties.class)
public class MattermostConfiguration {
    @Bean
    public MattermostClient mattermostClient(MattermostProperties properties) {
        MattermostClient mattermostClient = MattermostClient.builder()
                .url(properties.getUrl())
                .logLevel(Level.ALL)
                .build();

        mattermostClient.setAccessToken(properties.getToken());

        return mattermostClient;
    }
}
