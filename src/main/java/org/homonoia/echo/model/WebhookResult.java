package org.homonoia.echo.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@JsonRootName("result")
@Data
public class WebhookResult {
    private String id;
}
