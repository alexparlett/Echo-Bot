package org.homonoia.echo.service.listeners;

import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.Webhook;
import org.homonoia.echo.model.WebhookResult;
import org.homonoia.echo.service.client.HipchatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Component
public class HipchatInitializer implements SmartLifecycle {

    private final HipchatProperties hipchatProperties;
    private final HipchatClient hipchatClient;
    private final Map<String, List<WebhookResult>> activeWebhooks;
    private boolean running = false;

    @Autowired
    public HipchatInitializer(HipchatProperties hipchatProperties, HipchatClient hipchatClient) {
        this.hipchatProperties = hipchatProperties;
        this.hipchatClient = hipchatClient;
        this.activeWebhooks = new HashMap<>();
    }

    @Override
    public void start() {
        hipchatProperties.getRooms().forEach(room -> {
            unbindRoom(room);
            bindRoom(room);
        });
        running = true;
    }

    @Override
    public void stop() {
        hipchatProperties.getRooms().forEach(this::unbindRoom);
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
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

        List<WebhookResult> webhooks = hipchatClient.createWebhooks(room, roomEnter, roomExit, roomMessage, roomNotification);
        activeWebhooks.put(room, webhooks);
    }

    private void unbindRoom(String room) {
        List<WebhookResult> webhookResults = activeWebhooks.remove(room);
        if (nonNull(webhookResults)) {
            webhookResults.forEach(webhookResult -> hipchatClient.deleteWebhook(room, webhookResult));
        }
    }
}
