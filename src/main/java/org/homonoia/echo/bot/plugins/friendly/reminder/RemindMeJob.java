package org.homonoia.echo.bot.plugins.friendly.reminder;

import lombok.Setter;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static java.lang.String.format;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 01/11/2017
 */
public class RemindMeJob extends QuartzJobBean {

    @Setter
    private MattermostClient mattermostClient;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobData = context.getMergedJobDataMap();

        Post post = new Post();
        post.setChannelId(jobData.getString("channel"));
        post.setMessage(format("@%s %s", jobData.getString("user"), jobData.getString("message")));

        mattermostClient.createPost(post);
    }
}
