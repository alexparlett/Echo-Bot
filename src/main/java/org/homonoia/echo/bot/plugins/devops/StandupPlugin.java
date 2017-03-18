package org.homonoia.echo.bot.plugins.devops;

import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.post.Notification;
import org.homonoia.echo.model.post.NotificationColor;
import org.homonoia.echo.model.post.NotificationFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 18/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "hipchat.plugins.core", name = "devops", havingValue = "true", matchIfMissing = true)
public class StandupPlugin {

    @Autowired
    private HipchatClient hipchatClient;

    @Autowired
    private Map<String,Room> rooms;

    @Scheduled(cron = "0 0 10 * * MON-FRI", zone = "Europe/London")
    public void standup() {
        Notification notification = Notification.builder()
                .messageFormat(NotificationFormat.TEXT)
                .color(NotificationColor.PURPLE)
                .message("@all (standup)")
                .notify(true)
                .build();

        rooms.forEach((s, room) -> hipchatClient.sendRoomNotification(room, notification));
    }
}
