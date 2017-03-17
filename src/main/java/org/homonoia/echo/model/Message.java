package org.homonoia.echo.model;

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
@JsonRootName("message")
@Data
public class Message {
    private int id;
    private ZonedDateTime date;
    private User from;
    private List<User> mentions;
    private String message;
}
