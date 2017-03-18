package org.homonoia.echo.bot.plugins.friendly;

import org.homonoia.echo.bot.annotations.OnJoin;
import org.homonoia.echo.bot.annotations.OnLeave;
import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.model.RoomEnter;
import org.homonoia.echo.model.RoomExit;
import org.homonoia.echo.model.post.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "hipchat.plugins.core", name = "friendly", havingValue = "true", matchIfMissing = true)
public class PresenceChangesPlugin {

    @Autowired
    private HipchatClient hipchatClient;

    @OnJoin
    public void handleRoomJoined(RoomEnter event) {
        Message message = Message.builder()
                .message(MessageFormat.format("@{0} Welcome!", event.getSender().getMentionName()))
                .build();

        hipchatClient.sendRoomMessage(event.getRoom(), message);
    }

    @OnLeave
    public void handleRoomLeft(RoomExit event) {
        Message message = Message.builder()
                .message(MessageFormat.format("@{0} Goodbye!", event.getSender().getMentionName()))
                .build();

        hipchatClient.sendRoomMessage(event.getRoom(), message);
    }
}
