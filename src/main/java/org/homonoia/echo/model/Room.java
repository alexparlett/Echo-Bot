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
@JsonRootName("room")
@Data
public class Room {
    @JsonProperty("id")
    private int id;

    @JsonProperty("is_archived")
    private boolean archived;

    @JsonProperty("name")
    private String name;

    @JsonProperty("privacy")
    private String privacy;

    @JsonProperty("version")
    private String version;
}
