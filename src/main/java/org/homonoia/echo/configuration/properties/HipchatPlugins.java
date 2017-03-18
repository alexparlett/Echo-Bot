package org.homonoia.echo.configuration.properties;

import lombok.Data;

import java.util.Map;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 18/03/2017
 */
@Data
public class HipchatPlugins {
    private Map<String,Boolean> core;
}
