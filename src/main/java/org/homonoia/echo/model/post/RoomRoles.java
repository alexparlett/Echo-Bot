package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@JsonRootName("roomRoles")
@Data
@Builder
public class RoomRoles {
    @JsonProperty("room_roles")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private List<String> roomRoles;
}
