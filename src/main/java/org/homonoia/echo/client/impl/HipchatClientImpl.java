package org.homonoia.echo.client.impl;

import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.User;
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
    public void setRoomTopic(Room room, Topic topic) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/topic")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room.getId())
                .encode()
                .toUri();

        restTemplate.put(url, topic);
    }

    @Override
    public void sendRoomMessage(Room room, Message message) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/message")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room.getId())
                .encode()
                .toUri();

        restTemplate.postForLocation(url, message);
    }

    @Override
    public void sendRoomNotification(Room room, Notification notification) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}/message")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(room.getId())
                .encode()
                .toUri();

        restTemplate.postForLocation(url, notification);
    }

    @Override
    public Room getRoom(String name) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("room/{room}")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(name)
                .encode()
                .toUri();

        return restTemplate.getForObject(url, Room.class);
    }

    @Override
    public User getUserByMentionName(String mentionName) {
        URI url = UriComponentsBuilder.fromUriString(hipchatProperties.getUrl())
                .path("user/@{user}")
                .queryParam("auth_token", hipchatProperties.getToken())
                .buildAndExpand(mentionName)
                .encode()
                .toUri();

        return restTemplate.getForObject(url, User.class);
    }
}
