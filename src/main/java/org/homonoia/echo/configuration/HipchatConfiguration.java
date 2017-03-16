package org.homonoia.echo.configuration;

import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Configuration
@EnableConfigurationProperties(HipchatProperties.class)
public class HipchatConfiguration {
}
