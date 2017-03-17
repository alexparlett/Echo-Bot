package org.homonoia.echo.client;

import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.post.Message;
import org.homonoia.echo.model.post.Notification;
import org.homonoia.echo.model.post.Topic;
import org.homonoia.echo.model.post.Webhook;
import org.homonoia.echo.model.WebhookResult;

import java.util.List;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
public interface HipchatClient {
    List<WebhookResult> createWebhooks(String room, Webhook... webhook);
    void deleteWebhook(String room, WebhookResult webhookResult);
    void setRoomTopic(Room room, Topic topic);
    void sendRoomMessage(Room room, Message message);
    void sendRoomNotification(Room room, Notification notification);
}
