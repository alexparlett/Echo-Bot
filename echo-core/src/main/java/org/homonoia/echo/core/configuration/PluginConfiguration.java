package org.homonoia.echo.core.configuration;

import org.homonoia.echo.core.configuration.properties.PluginsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2015-2019 Homonoia Studios.
 *
 * @author alexparlett
 * @since 19/10/2019
 */
@Configuration
@EnableConfigurationProperties(PluginsProperties.class)
public class PluginConfiguration {
}
