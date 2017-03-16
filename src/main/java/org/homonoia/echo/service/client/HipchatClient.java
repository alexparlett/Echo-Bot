package org.homonoia.echo.service.client;

import org.homonoia.echo.model.Webhook;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
public interface HipchatClient {
    void createWebhooks(String room, Webhook... webhook);
}
