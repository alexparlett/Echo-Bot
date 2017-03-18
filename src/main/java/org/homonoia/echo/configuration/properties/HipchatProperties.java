package org.homonoia.echo.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@ConfigurationProperties(prefix = "hipchat")
@Getter
@Setter
public class HipchatProperties {
    private String url;
    private String token;
    private List<String> rooms;
    private HipchatCallbacks callbacks;
    private String user;
}
