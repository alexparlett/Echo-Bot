package org.homonoia.echo.app;

import org.homonoia.echo.core.configuration.EchoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@SpringBootApplication
@ImportAutoConfiguration(EchoConfiguration.class)
public class EchoMain {
    public static void main(String[] args) {
        SpringApplication.run(EchoMain.class, args);
    }
}
