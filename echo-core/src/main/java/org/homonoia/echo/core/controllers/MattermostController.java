package org.homonoia.echo.core.controllers;

import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.model.OutgoingWebhookPayload;
import org.homonoia.echo.core.bot.event.MattermostEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@RestController
@RequestMapping("/mattermost")
@Slf4j
public class MattermostController {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MattermostController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleMessages(@RequestBody OutgoingWebhookPayload payload) {
        applicationEventPublisher.publishEvent(MattermostEvent.builder()
                .payload(payload)
                .self(true)
                .build());
    }
}
