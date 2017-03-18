package org.homonoia.echo.configuration;

import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Configuration
@EnableConfigurationProperties(HipchatProperties.class)
public class HipchatConfiguration {
    @Bean
    public Map<String,Room> rooms(HipchatProperties hipchatProperties, HipchatClient hipchatClient) {
        return hipchatProperties.getRooms().stream()
                .map(hipchatClient::getRoom)
                .collect(Collectors.toMap(room -> room.getName(), room -> room));
    }

    @Bean
    public User user(HipchatProperties hipchatProperties, HipchatClient hipchatClient) {
        return hipchatClient.getUserByMentionName(hipchatProperties.getMentionName());
    }
}
