package org.homonoia.echo.bot.plugins.friendly.reminder;

import lombok.Setter;
import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.post.Message;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.MessageFormat;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 01/11/2017
 */
public class RemindMeJob extends QuartzJobBean {

    @Setter
    private HipchatClient hipchatClient;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobData = context.getMergedJobDataMap();

        Message message = Message.builder()
                .message(MessageFormat.format("@{0} {1}", jobData.getString("user"), jobData.getString("message")))
                .build();

        hipchatClient.sendRoomMessage((Room) jobData.get("room"), message);
    }
}
