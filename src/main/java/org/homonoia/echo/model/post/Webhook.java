package org.homonoia.echo.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("webhook")
@Data
@Builder
public class Webhook {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pattern;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authentication;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String event;
}
