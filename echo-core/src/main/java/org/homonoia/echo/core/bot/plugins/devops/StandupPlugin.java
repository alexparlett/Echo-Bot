package org.homonoia.echo.core.bot.plugins.devops;

import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Channel;
import net.bis5.mattermost.model.Post;
import org.homonoia.echo.core.configuration.properties.PluginsProperties;
import org.homonoia.echo.core.documentation.annotations.EchoDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 18/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "plugins.devops.standup", name = "enabled", havingValue = "true", matchIfMissing = false)
public class StandupPlugin {

    private final MattermostClient mattermostClient;
    private final PluginsProperties pluginsProperties;

    @Autowired
    public StandupPlugin(MattermostClient mattermostClient, PluginsProperties pluginsProperties) {
        this.mattermostClient = mattermostClient;
        this.pluginsProperties = pluginsProperties;
    }

    @Scheduled(cron = "${plugins.devops.standup.configuration.cron}", zone = "${plugins.devops.standup.configuration.timezone}")
    @EchoDoc(
            value = "Standup",
            description = "Posts a standup reminder at a set interval",
            namespace = "Devops"
    )
    public void standup() {
        LinkedHashMap<String, LinkedHashMap<String,String>> teamMap = (LinkedHashMap<String, LinkedHashMap<String,String>>) pluginsProperties.getDevops().get("standup").getConfiguration().get("teams");
        teamMap.forEach((team, channels) -> {
            channels.values().forEach(channel -> {
                Channel response = mattermostClient.getChannelByNameForTeamName(channel, team).readEntity();

                Post post = new Post();
                post.setChannelId(response.getId());
                post.setMessage("@all Standup");

                mattermostClient.createPost(post);
            });
        });
    }
}
