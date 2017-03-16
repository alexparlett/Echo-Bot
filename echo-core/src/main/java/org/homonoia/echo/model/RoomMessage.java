package org.homonoia.echo.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("roomEnter")
@Getter
@Setter
public class RoomMessage {
    private Room room;
    private Message message;
}
