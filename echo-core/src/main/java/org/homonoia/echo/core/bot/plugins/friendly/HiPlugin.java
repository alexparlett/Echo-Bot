package org.homonoia.echo.core.bot.plugins.friendly;

import org.homonoia.echo.core.bot.annotations.RespondTo;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import net.bis5.mattermost.model.User;
import org.homonoia.echo.core.bot.event.MattermostEvent;
import org.homonoia.echo.core.documentation.annotations.EchoDoc;
import org.homonoia.echo.core.documentation.annotations.EchoDocExample;
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
@ConditionalOnProperty(prefix = "plugins.friendly.hello", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HiPlugin {

    @Autowired
    private MattermostClient mattermostClient;

    @RespondTo(regex = "#root.text contains '\\b(Hi|Hello|Howdy|Gday)'")
    @EchoDoc(
            value = "Hello",
            description = "Responds to a Hello",
            namespace = "Friendly",
            examples = {
                    @EchoDocExample(value = "!Echo Hi"),
                    @EchoDocExample(value = "!Echo Howdy")
            }
    )
    public void handleDirectHello(MattermostEvent event) {
        User user = mattermostClient.getUser(event.getPayload().getUserId()).readEntity();

        Post post = new Post();
        post.setChannelId(event.getPayload().getChannelId());
        post.setMessage(String.format("Hi @%s", user.getUsername()));

        mattermostClient.createPost(post);
    }
}
