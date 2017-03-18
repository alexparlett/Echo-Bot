package org.homonoia.echo.configuration;

import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Configuration
@Import(WebClientAutoConfiguration.class)
public class WebClientConfiguration {

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsAsyncClientHttpRequestFactory();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, ClientHttpRequestFactory clientHttpRequestFactory) {
        return restTemplateBuilder.requestFactory(clientHttpRequestFactory)
                .build();
    }

}
