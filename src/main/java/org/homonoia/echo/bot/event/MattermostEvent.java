package org.homonoia.echo.bot.event;

import lombok.Builder;
import lombok.Getter;
import net.bis5.mattermost.model.OutgoingWebhookPayload;

/**
 * Copyright (c) 2015-2019 Homonoia Studios.
 *
 * @author alexparlett
 * @since 19/10/2019
 */
@Builder
@Getter
public class MattermostEvent {

    private boolean self;

    private OutgoingWebhookPayload payload;

}
