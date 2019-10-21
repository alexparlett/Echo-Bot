package org.homonoia.echo.core.bot.plugins.friendly;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import net.bis5.mattermost.model.User;
import org.homonoia.echo.core.bot.annotations.RespondTo;
import org.homonoia.echo.core.bot.event.MattermostEvent;
import org.homonoia.echo.core.bot.plugins.friendly.reminder.RemindMeJob;
import org.homonoia.echo.core.documentation.annotations.EchoDoc;
import org.homonoia.echo.core.documentation.annotations.EchoDocExample;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "plugins.friendly.reminder", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ReminderPlugin {

    private static final Pattern REMIND_ME_PATTERN = Pattern.compile(".*?remind\\s+(me|@[A-Za-z]+)\\s+(.+)\\s+to\\s+(.+)");

    private final MattermostClient mattermostClient;

    private final Scheduler scheduler;

    @Autowired
    public ReminderPlugin(MattermostClient mattermostClient, Scheduler scheduler) {
        this.mattermostClient = mattermostClient;
        this.scheduler = scheduler;
    }

    @RespondTo(regex = "#root.text contains '.*?(remind) (me|@[A-Za-z]+)'")
    @EchoDoc(
            value = "Reminder",
            description = "Remind either yourself of a specific person with a message at a set time",
            namespace = "Friendly",
            examples = {
                    @EchoDocExample(value = "!Echo remind me today at 3pm to do the build"),
                    @EchoDocExample(value = "!Echo remind @Tom today at 3pm to do the build")
            }
    )
    public void handleReminder(MattermostEvent event) throws SchedulerException {
        Matcher matchResult = REMIND_ME_PATTERN.matcher(event.getPayload().getText());
        if (matchResult.find() && matchResult.groupCount() == 3) {
            String target = matchResult.group(1);
            User user = getUser(event, target);
            String time = matchResult.group(2);
            String message = matchResult.group(3);

            Span parse = Chronic.parse(time, new Options(true));
            parse.getEndCalendar().toInstant();
            schedule(event, user, message, Date.from(parse.getEndCalendar().toInstant()));

            Post post = new Post();
            post.setChannelId(event.getPayload().getChannelId());
            post.setMessage(format("@%s Reminder set.", event.getPayload().getUserName()));

            mattermostClient.createPost(post);
        } else {
            User user = mattermostClient.getUser(event.getPayload().getUserId()).readEntity();

            Post post = new Post();
            post.setChannelId(event.getPayload().getChannelId());
            post.setMessage(String.format("@%s I'm not sure how to respond to that! Please specify when you want the message to be sent and then the message.", user.getUsername()));

            mattermostClient.createPost(post);
        }
    }

    private User getUser(MattermostEvent event, String target) {
        return target.equalsIgnoreCase("me") ?
                mattermostClient.getUser(event.getPayload().getUserId()).readEntity() :
                mattermostClient.getUserByUsername(target.replace("@", "")).readEntity();
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
                .storeDurably()
                .build();

        scheduler.addJob(jobDetail, true);

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(event.getPayload().getPostId())
                .startAt(scheduledTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .build();

        scheduler.scheduleJob(trigger);
    }
}
