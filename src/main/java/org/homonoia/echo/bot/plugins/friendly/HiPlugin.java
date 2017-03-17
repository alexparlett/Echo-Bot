package org.homonoia.echo.bot.plugins.friendly;

import org.homonoia.echo.bot.annotations.Hear;
import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.post.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
public class HiPlugin {

    @Autowired
    private HipchatClient hipchatClient;

    @Hear(regex = "#roomMessage.message.message matches '(Hi|Hello|Howdy|Gday).?Echo'")
    public void handleDirectHello(RoomMessage roomMessage) {
        Message message = Message.builder()
                .message(MessageFormat.format("Hi @{0}", roomMessage.getMessage().getFrom().getMentionName()))
                .build();

        hipchatClient.sendRoomMessage(roomMessage.getRoom(), message);
    }

}
