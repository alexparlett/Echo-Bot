package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String from;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("message_format")
    private NotificationFormat messageFormat;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private NotificationColor color;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("attach_to")
    private String attachTo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default
    private boolean notify = false;

    private String message;
}
