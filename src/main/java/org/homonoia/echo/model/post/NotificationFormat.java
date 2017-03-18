package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 18/03/2017
 */
public enum NotificationFormat {

    TEXT("text"),
    HTML("html");

    private String value;

    NotificationFormat(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
