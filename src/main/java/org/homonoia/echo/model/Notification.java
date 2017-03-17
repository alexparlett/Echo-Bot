package org.homonoia.echo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("notification")
@Data
public class Notification {
    private int id;
    private ZonedDateTime date;
    private String from;
    private List<User> mentions;
    private String message;
    private String color;
    private String type;
    @JsonProperty("message_format")
    private String messageFormat;
}
