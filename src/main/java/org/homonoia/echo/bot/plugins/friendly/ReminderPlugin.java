package org.homonoia.echo.bot.plugins.friendly;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import net.bis5.mattermost.model.User;
import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.MattermostEvent;
import org.homonoia.echo.bot.plugins.friendly.reminder.RemindMeJob;
import org.homonoia.echo.documentation.annotations.EchoDoc;
import org.homonoia.echo.documentation.annotations.EchoDocExample;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "plugins.core", name = "friendly", havingValue = "true", matchIfMissing = true)
public class ReminderPlugin {

    private static final Pattern REMIND_ME_PATTERN = Pattern.compile(".*?remind \\b(me|@.+) \\b(.+) to \\b(.+)");

    @Autowired
    private MattermostClient mattermostClient;

    @Autowired
    private Scheduler scheduler;

    //@Echo remind me (at 3pm | in 15 minutes) to (do the build)
    @RespondTo(regex = "#root contains '\\b(remind) \\b(me|@.+)'")
    @EchoDoc(
            value = "Reminder",
            description = "Remind either yourself of a specific person with a message at a set time",
            namespace = "plugins.core.friendly",
            examples = {
                    @EchoDocExample(value = "@Echo remind me at 3pm to do the build"),
                    @EchoDocExample(value = "@Echo remind @Tom at 3pm to do the build")
            }
    )
    public void handleReminder(MattermostEvent event) throws SchedulerException {
        MatchResult matchResult = REMIND_ME_PATTERN.matcher(event.getPayload().getText()).toMatchResult();
        if (matchResult.groupCount() == 3) {
            String target = matchResult.group(1);
            User user = mattermostClient.getUser(target).readEntity();
            String time = matchResult.group(2);
            String message = matchResult.group(3);

            Span parse = Chronic.parse(time, new Options(true));
            parse.getEndCalendar().toInstant();
            schedule(event, user, message, Date.from(parse.getEndCalendar().toInstant()));
        } else {
            User user = mattermostClient.getUser(event.getPayload().getUserId()).readEntity();

            Post post = new Post();
            post.setChannelId(event.getPayload().getChannelId());
            post.setMessage(format("@%s I'm not sure how to respond to that! Please specify when you want the message to be sent and then the message.", user.getNickname()));

            mattermostClient.createPost(post);
        }
    }

    private void schedule(MattermostEvent event, User user, String message, Date scheduledTime) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("user", user.getUsername());
        jobDataMap.put("message", message);
        jobDataMap.put("channel", event.getPayload().getChannelId());

        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(event.getPayload().getPostId())
                .ofType(RemindMeJob.class)
                .setJobData(jobDataMap)
                .build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(event.getPayload().getPostId())
                .startAt(scheduledTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .build();

        scheduler.scheduleJob(trigger);
    }
}
