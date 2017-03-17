package org.homonoia.echo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("webhookEvent")
@Data
public class WebhookEvent<T> {
    @JsonProperty("event")
    private String event;

    @JsonProperty("webhook_id")
    private int webhookId;

    @JsonProperty("item")
    private T item;
}
