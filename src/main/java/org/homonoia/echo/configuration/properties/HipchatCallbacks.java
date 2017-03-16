package org.homonoia.echo.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Getter
@Setter
public class HipchatCallbacks {
    private String message;
    private String notification;
    private String enter;
    private String exit;
}
