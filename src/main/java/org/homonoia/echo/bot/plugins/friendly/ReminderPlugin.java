package org.homonoia.echo.bot.plugins.friendly;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;
import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.documentation.annotations.EchoDoc;
import org.homonoia.echo.documentation.annotations.EchoDocExample;
import org.homonoia.echo.bot.plugins.friendly.reminder.RemindMeJob;
import org.homonoia.echo.client.HipchatClient;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.User;
import org.homonoia.echo.model.post.Message;
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

import java.text.MessageFormat;
import java.util.Date;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "hipchat.plugins.core", name = "friendly", havingValue = "true", matchIfMissing = true)
public class ReminderPlugin {

    public static final Pattern REMIND_ME_PATTERN = Pattern.compile(".*?remind \\b(me|@.+) \\b(.+) to \\b(.+)");

    @Autowired
    private HipchatClient hipchatClient;

    @Autowired
    private Scheduler scheduler;

    //@Echo remind me (at 3pm | in 15 minutes) to (do the build)
    @RespondTo(regex = "#root.message contains '\\b(remind) \\b(me|@.+)'")
    @EchoDoc(
            value = "Reminder",
            description = "Remind either yourself of a specific person with a message at a set time",
            namespace = "hipchat.plugins.core.friendly",
            examples = {
                    @EchoDocExample(value = "@Echo remind me at 3pm to do the build"),
                    @EchoDocExample(value = "@Echo remind @Tom at 3pm to do the build")
            }
    )
    public void handleReminder(RoomMessage event) throws SchedulerException {
        MatchResult matchResult = REMIND_ME_PATTERN.matcher(event.getMessage().getMessage()).toMatchResult();
        if (matchResult.groupCount() == 3) {
            String target = matchResult.group(1);
            User user = target.equalsIgnoreCase("me") ? event.getMessage().getFrom() : hipchatClient.getUserByMentionName(target);
            String time = matchResult.group(2);
            String message = matchResult.group(3);

            Span parse = Chronic.parse(time, new Options(true));
            parse.getEndCalendar().toInstant();
            schedule(event, user, message, Date.from(parse.getEndCalendar().toInstant()));
        } else {
            Message message = Message.builder()
                    .message(MessageFormat.format("@{0} I'm not sure how to respond to that! Please specify when you want the message to be sent and then the message.", event.getMessage().getFrom().getMentionName()))
                    .build();

            hipchatClient.sendRoomMessage(event.getRoom(), message);
        }
    }

    private void schedule(RoomMessage event, User user, String message, Date scheduledTime) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("user", user.getMentionName());
        jobDataMap.put("message", message);
        jobDataMap.put("room", event.getRoom());

        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(event.getMessage().getId())
                .ofType(RemindMeJob.class)
                .setJobData(jobDataMap)
                .build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(event.getMessage().getId())
                .startAt(scheduledTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .build();

        scheduler.scheduleJob(trigger);
    }
}
