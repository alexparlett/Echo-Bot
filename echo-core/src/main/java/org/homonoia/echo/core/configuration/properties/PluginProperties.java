package org.homonoia.echo.core.configuration.properties;

import lombok.Data;

import java.util.Map;

/**
 * Copyright (c) 2015-2019 Homonoia Studios.
 *
 * @author alexparlett
 * @since 20/10/2019
 */
@Data
public class PluginProperties {
    private boolean enabled;

    private Map<String,Object> configuration;
}
