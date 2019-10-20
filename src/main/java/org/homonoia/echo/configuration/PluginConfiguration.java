package org.homonoia.echo.configuration;

import org.homonoia.echo.configuration.properties.PluginProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2015-2019 Homonoia Studios.
 *
 * @author alexparlett
 * @since 19/10/2019
 */
@Configuration
@EnableConfigurationProperties(PluginProperties.class)
public class PluginConfiguration {
}
