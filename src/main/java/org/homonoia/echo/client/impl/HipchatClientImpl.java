package org.homonoia.echo.client.impl;

import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.post.Message;
import org.homonoia.echo.model.post.Notification;
import org.homonoia.echo.model.post.Topic;
import org.homonoia.echo.model.post.Webhook;
import org.homonoia.echo.model.WebhookResult;
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

        return Stream.of(webhooks).map(webhook -> restTemplate.postForObject(url, webhook, WebhookResult.class))
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

        restTemplate.delete(url);
    }

    @Override
    public void setRoomTopic(String room, Topic topic) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/topic")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room)
                .encode()
                .toUri();

        restTemplate.put(url, topic);
    }

    @Override
    public void sendRoomMessage(String room, Message message) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/message")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room)
                .encode()
                .toUri();

        restTemplate.postForLocation(url, message);
    }

    @Override
    public void sendRoomNotification(String room, Notification notification) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/message")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room)
                .encode()
                .toUri();

        restTemplate.postForLocation(url, notification);
    }
}
