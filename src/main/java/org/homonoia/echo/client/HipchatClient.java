package org.homonoia.echo.client;

import org.homonoia.echo.model.Topic;
import org.homonoia.echo.model.User;
import org.homonoia.echo.model.WebhookResult;
import org.homonoia.echo.model.Webhook;

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
    User getUserByMentionName(String mentionName);
    void addRoomMemberByMentionName(String room, String mentionName);

    void removeRoomMemberByMentionName(String room, String mentionName);
}
