package org.homonoia.echo.service.client;

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
}
