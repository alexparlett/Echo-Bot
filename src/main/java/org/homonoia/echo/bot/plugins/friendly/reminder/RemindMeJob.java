package org.homonoia.echo.bot.plugins.friendly.reminder;

import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.model.Room;
import org.homonoia.echo.model.post.Message;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 01/11/2017
 */
public class RemindMeJob implements Job {

    @Autowired
    private HipchatClient hipchatClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobData = context.getMergedJobDataMap();

        Message message = Message.builder()
                .message(MessageFormat.format("@{0} you asked me to remind you: {1}", jobData.getString("user"), jobData.getString("message")))
                .build();

        hipchatClient.sendRoomMessage((Room) jobData.get("room"), message);
    }
}
