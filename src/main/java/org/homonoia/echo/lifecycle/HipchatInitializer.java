package org.homonoia.echo.lifecycle;

import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.post.Notification;
import org.homonoia.echo.model.post.NotificationColor;
import org.homonoia.echo.model.post.NotificationFormat;
import org.homonoia.echo.model.post.Webhook;
import org.homonoia.echo.model.WebhookResult;
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

    private HipchatProperties hipchatProperties;
    private final HipchatClient hipchatClient;
    private final Map<String, List<WebhookResult>> activeWebhooks;
    private final Map<String, Room> rooms;
    private boolean running = false;

    @Autowired
    public HipchatInitializer(HipchatProperties hipchatProperties, HipchatClient hipchatClient, Map<String,Room> rooms) {
        this.hipchatProperties = hipchatProperties;
        this.hipchatClient = hipchatClient;
        this.activeWebhooks = new HashMap<>();
        this.rooms = rooms;
    }

    @Override
    public void start() {
        rooms.forEach((name,room) -> {
            unbindRoom(room);
            bindRoom(room);
        });
        running = true;
    }

    @Override
    public void stop() {
        rooms.forEach((name,room) -> unbindRoom(room));
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

    private void bindRoom(Room room) {
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

        List<WebhookResult> webhooks = hipchatClient.createWebhooks(room.getName(), roomEnter, roomExit, roomMessage, roomNotification);
        activeWebhooks.put(room.getName(), webhooks);

        Notification notification = Notification.builder()
                .message("@all Hi!")
                .color(NotificationColor.PURPLE)
                .messageFormat(NotificationFormat.TEXT)
                .notify(false)
                .build();

        hipchatClient.sendRoomNotification(room, notification);
    }

    private void unbindRoom(Room room) {
        List<WebhookResult> webhookResults = activeWebhooks.remove(room);
        if (nonNull(webhookResults)) {
            webhookResults.forEach(webhookResult -> hipchatClient.deleteWebhook(room.getName(), webhookResult));
        }
    }
}
