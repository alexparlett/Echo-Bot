package org.homonoia.echo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("roomNotification")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoomNotification extends RoomEvent {
    private Notification message;
    @JsonIgnore
    private boolean self;
}
