package org.homonoia.echo.bot.plugins.core;

import com.hubspot.jinjava.Jinjava;
import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.documentation.DocumentationProcessor;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.post.Notification;
import org.homonoia.echo.model.post.NotificationColor;
import org.homonoia.echo.model.post.NotificationFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "hipchat.plugins.core", name = "friendly", havingValue = "true", matchIfMissing = true)
public class HelpPlugin {

    @Autowired
    private HipchatClient hipchatClient;

    @Autowired
    private Jinjava jinjava;

    @Autowired
    private DocumentationProcessor documentationProcessor;

    @Value("classpath:templates/help.template.html")
    private String helpTemplate;

    @RespondTo(regex = "#root.message contains '\\b(Help)'")
    public void handleDirectHelp(RoomMessage roomMessage) {
        Notification message = Notification.builder()
                .message(jinjava.render(helpTemplate, documentationProcessor.getEchoDocumentation()))
                .messageFormat(NotificationFormat.HTML)
                .color(NotificationColor.PURPLE)
                .notify(false)
                .attachTo(roomMessage.getMessage().getId())
                .build();

        hipchatClient.sendRoomNotification(roomMessage.getRoom(), message);
    }
}
