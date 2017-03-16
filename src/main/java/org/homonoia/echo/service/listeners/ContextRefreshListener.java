package org.homonoia.echo.service.listeners;

import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.service.client.HipchatClient;
import org.homonoia.echo.model.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Component
public class ContextRefreshListener {

    private final HipchatProperties hipchatProperties;
    private final HipchatClient hipchatClient;

    @Autowired
    public ContextRefreshListener(HipchatProperties hipchatProperties, HipchatClient hipchatClient) {
        this.hipchatProperties = hipchatProperties;
        this.hipchatClient = hipchatClient;
    }

    @EventListener
    public void handleContextRefreshedEvent(ContextRefreshedEvent contextRefreshedEvent) {
        hipchatProperties.getRooms().forEach(this::bindRoom);
    }

    private void bindRoom(String room) {
        Webhook roomEnter = Webhook.builder()
                .event("room_enter")
                .url(hipchatProperties.getCallbacks().getEnter())
                .build();

        Webhook roomExit = Webhook.builder()
                .event("room_exit")
                .url(hipchatProperties.getCallbacks().getExit())
                .build();

        Webhook roomMessage = Webhook.builder()
                .event("room_message")
                .url(hipchatProperties.getCallbacks().getMessage())
                .build();

        Webhook roomNotification = Webhook.builder()
                .event("room_notification")
                .url(hipchatProperties.getCallbacks().getNotification())
                .build();

        hipchatClient.createWebhooks(room, roomEnter, roomExit, roomMessage, roomNotification);
    }

}
