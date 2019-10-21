package org.homonoia.echo.core.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 18/03/2017
 */
@ConfigurationProperties(prefix = "plugins")
@Getter
@Setter
public class PluginsProperties {
    private Map<String, PluginProperties> core;
    private Map<String, PluginProperties> friendly;
    private Map<String, PluginProperties> devops;
}
