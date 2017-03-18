package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@JsonRootName("notification")
@Data
@Builder
public class Notification {
    private String from;

    @JsonProperty("message_format")
    private NotificationFormat messageFormat;

    private NotificationColor color;

    @JsonProperty("attach_to")
    private String attachTo;

    private boolean notify = false;

    private String message;
}
