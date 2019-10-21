package org.homonoia.echo.core.bot.plugins.core;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.interpret.RenderResult;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import org.homonoia.echo.core.bot.annotations.RespondTo;
import org.homonoia.echo.core.bot.event.MattermostEvent;
import org.homonoia.echo.core.documentation.DocumentationProcessor;
import org.homonoia.echo.core.documentation.annotations.EchoDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "plugins.core.help", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HelpPlugin {

    private final MattermostClient mattermostClient;

    private final Jinjava jinjava;

    private final DocumentationProcessor documentationProcessor;

    @Value("#{T(com.google.common.io.Resources).toString(T(com.google.common.io.Resources).getResource('templates/help.template.md'), T(java.nio.charset.StandardCharsets).UTF_8)}")
    private String helpTemplate;

    @Autowired
    public HelpPlugin(MattermostClient mattermostClient, Jinjava jinjava, DocumentationProcessor documentationProcessor) {
        this.mattermostClient = mattermostClient;
        this.jinjava = jinjava;
        this.documentationProcessor = documentationProcessor;
    }

    @RespondTo(regex = "#root.text contains '\\b(Help)'")
    public void handleDirectHelp(MattermostEvent event) {
        Map<String, MultiValueMap<String, EchoDoc>> context = singletonMap("docs", documentationProcessor.getEchoDocumentation());
        RenderResult render = jinjava.renderForResult(helpTemplate, context);

        Post post = new Post();
        post.setChannelId(event.getPayload().getChannelId());
        post.setMessage(render.getOutput());

        mattermostClient.createPost(post);
    }
}
