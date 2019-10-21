package org.homonoia.echo.core.configuration;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Copyright (c) 2015-2019 Homonoia Studios.
 *
 * @author alexparlett
 * @since 21/10/2019
 */
@Configuration
@EnableScheduling
@EnableAsync
@EnableWebMvc
@ComponentScan("org.homonoia.echo.core")
@ImportAutoConfiguration(ConfigurationPropertiesAutoConfiguration.class)
public class EchoConfiguration {
}
