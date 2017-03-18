package org.homonoia.echo.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.homonoia.echo.model.RoomEnter;
import org.homonoia.echo.model.RoomExit;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.RoomNotification;
import org.homonoia.echo.model.User;
import org.homonoia.echo.model.WebhookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@RestController
@RequestMapping("/hipchat")
@Slf4j
public class HipchatWebhookController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private User user;

    @PostMapping(value = "/room-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomMessage(@RequestBody WebhookEvent<RoomMessage> roomMessage) {
        boolean botMentioned = roomMessage
                .getItem()
                .getMessage()
                .getMentions()
                .stream()
                .anyMatch(mentioned -> Objects.equals(mentioned.getMentionName(), user.getMentionName()));
        roomMessage.getItem().setSelf(botMentioned);

        log.info("{}", roomMessage);

        applicationEventPublisher.publishEvent(roomMessage.getItem());
    }

    @PostMapping(value = "/room-notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomNotification(@RequestBody WebhookEvent<RoomNotification> roomNotification) {
        boolean botMentioned = roomNotification
                .getItem()
                .getMessage()
                .getMentions()
                .stream()
                .anyMatch(mentioned -> Objects.equals(mentioned.getMentionName(), user.getMentionName()));
        roomNotification.getItem().setSelf(botMentioned);

        log.info("{}", roomNotification);

        applicationEventPublisher.publishEvent(roomNotification.getItem());
    }

    @PostMapping(value = "/room-enter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomEnter(@RequestBody WebhookEvent<RoomEnter> roomEnter) {
        log.info("{}", roomEnter);
        applicationEventPublisher.publishEvent(roomEnter.getItem());
    }

    @PostMapping(value = "/room-exit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomExit(@RequestBody WebhookEvent<RoomExit> roomExit) {
        log.info("{}", roomExit);
        applicationEventPublisher.publishEvent(roomExit.getItem());
    }
}
