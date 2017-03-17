package org.homonoia.echo.client.impl;

import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.WebhookResult;
import org.homonoia.echo.model.Webhook;
import org.homonoia.echo.client.HipchatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<WebhookResult> createWebhooks(String room, Webhook... webhooks) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/webhook")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room)
                .encode()
                .toUri();

        return Stream.of(webhooks).map(webhook -> restTemplate.postForEntity(url, webhook, WebhookResult.class))
                .map(resultResponseEntity -> resultResponseEntity.getBody())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWebhook(String room, WebhookResult webhookResult) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/webhook/{id}")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room, webhookResult.getId())
                .encode()
                .toUri();

        restTemplate.postForLocation(url, null);
    }
}
