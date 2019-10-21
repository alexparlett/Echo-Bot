package org.homonoia.echo.core.configuration;

import com.hubspot.jinjava.Jinjava;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2015-2018 Homonoia Studios.
 *
 * @author alexparlett
 * @since 11/11/2018
 */
@Configuration
public class JinJavaConfiguration {
    @Bean
    public Jinjava jinjava() {
        return new Jinjava();
    }
}
