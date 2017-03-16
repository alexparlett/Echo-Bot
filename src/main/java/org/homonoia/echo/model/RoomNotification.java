package org.homonoia.echo.model;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("roomNotification")
public class RoomNotification {
    private Room room;
    private Notification message;
}
