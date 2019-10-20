package org.homonoia.echo.lifecycle;

import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.ChannelList;
import net.bis5.mattermost.model.ContentType;
import net.bis5.mattermost.model.OutgoingWebhook;
import net.bis5.mattermost.model.Post;
import net.bis5.mattermost.model.Team;
import net.bis5.mattermost.model.TriggerWhen;
import net.bis5.mattermost.model.User;
import org.homonoia.echo.configuration.properties.MattermostProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.Maps.newConcurrentMap;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@Component
@Slf4j
public class MattermostInitializer implements SmartLifecycle {

    private MattermostProperties mattermostProperties;
    private final MattermostClient mattermostClient;
    private final Map<String, String> teams;
    private boolean running = false;

    @Autowired
    public MattermostInitializer(MattermostProperties mattermostProperties, MattermostClient mattermostClient) {
        this.mattermostProperties = mattermostProperties;
        this.mattermostClient = mattermostClient;
        this.teams = newConcurrentMap();
    }

    @Override
    public void start() {
        ApiResponse<User> userByUsername = mattermostClient.getUserByUsername(mattermostProperties.getUser());
        if (userByUsername.hasError()) {
            log.error("User not found {}", mattermostProperties.getUser());
            return;
        }
        mattermostProperties.getTeams().forEach(team -> bindTeam(team, userByUsername.readEntity()));
        running = true;
    }

    @Override
    public void stop() {
        teams.forEach((team, hook) -> unbindTeam(hook));
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
    }

    private void bindTeam(String team, User user) {
        ApiResponse<Team> teamByName = mattermostClient.getTeamByName(team);
        if (teamByName.hasError()) {
            log.error("Failed to read team {}", team);
            return;
        }

        String teamId = teamByName.readEntity().getId();

        OutgoingWebhook request = new OutgoingWebhook();
        request.setTeamId(teamId);
        request.setContentType(ContentType.JSON);
        request.setDisplayName("Echo");
        request.setCallbackUrls(singletonList(mattermostProperties.getCallback()));
        request.setTriggerWhen(TriggerWhen.STARTS_WITH);
        request.setTriggerWords(singletonList(mattermostProperties.getTrigger()));

        ApiResponse<OutgoingWebhook> outgoingWebhook = mattermostClient.createOutgoingWebhook(request);
        if (outgoingWebhook.hasError()) {
            log.error("Failed to create webhook for team {}", team);
        }

        teams.put(teamId, outgoingWebhook.readEntity().getId());

        ApiResponse<ChannelList> channelsForTeam = mattermostClient.getPublicChannelsForTeam(teamId);
        if (channelsForTeam.hasError()) {
            log.error("Failed to read channels for team {}", team);
            return;
        }

        Post post = new Post();
        post.setMessage(format("@all Hi! I'm now active in this channel, ask %s Help to find out what I can do.", mattermostProperties.getTrigger()));

        channelsForTeam.readEntity().forEach(channel -> {
            post.setChannelId(channel.getId());
            mattermostClient.addChannelMember(channel.getId(), user.getId());
            mattermostClient.createPost(post);
        });
    }

    private void unbindTeam(String hookId) {
        if (isNotBlank(hookId)) {
            mattermostClient.deleteOutgoingWebhook(hookId);
        }
    }
}
