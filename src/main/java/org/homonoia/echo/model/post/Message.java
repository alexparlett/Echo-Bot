package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@JsonRootName("message")
@Data
@Builder
public class Message {
    private String message;
}
