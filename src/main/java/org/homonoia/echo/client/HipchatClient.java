package org.homonoia.echo.client;

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
    void setRoomTopic(String room, Topic topic);
    void sendRoomMessage(String room, Message message);
    void sendRoomNotification(String room, Notification notification);
}
