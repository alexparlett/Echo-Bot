package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 18/03/2017
 */
public enum NotificationColor {

    YELLOW("yellow"),
    GREEN("green"),
    RED("red"),
    PURPLE("purple"),
    GRAY("gray"),
    RANDOM("random");

    private String value;

    NotificationColor(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
