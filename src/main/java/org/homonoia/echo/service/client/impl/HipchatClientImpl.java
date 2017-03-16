package org.homonoia.echo.service.client.impl;

import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.service.client.HipchatClient;
import org.homonoia.echo.model.Result;
import org.homonoia.echo.model.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Service
public class HipchatClientImpl implements HipchatClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HipchatProperties hipchatProperties;

    @Override
    public void createWebhooks(String room, Webhook... webhooks) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/webhook")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room)
                .encode()
                .toUri();

        Stream.of(webhooks).forEach(webhook -> {
            ResponseEntity<Result> resultResponseEntity = restTemplate.postForEntity(url, webhook, Result.class);
        });
    }
}
