package org.homonoia.echo.bot.plugins.friendly;

import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import net.bis5.mattermost.model.User;
import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.MattermostEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "plugins.core", name = "friendly", havingValue = "true", matchIfMissing = true)
public class HiPlugin {

    @Autowired
    private MattermostClient mattermostClient;

    @RespondTo(regex = "#root contains '\\b(Hi|Hello|Howdy|Gday)'")
    public void handleDirectHello(MattermostEvent event) {
        User user = mattermostClient.getUser(event.getPayload().getUserId()).readEntity();

        Post post = new Post();
        post.setChannelId(event.getPayload().getChannelId());
        post.setMessage(format("Hi @%s", user.getUsername()));

        mattermostClient.createPost(post);
    }
}
