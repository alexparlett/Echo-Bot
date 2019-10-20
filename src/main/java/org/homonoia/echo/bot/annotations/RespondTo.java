package org.homonoia.echo.bot.annotations;

import net.bis5.mattermost.model.OutgoingWebhookPayload;
import org.homonoia.echo.bot.event.MattermostEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener(condition = "#a0.self == true", classes = MattermostEvent.class)
@Async
public @interface RespondTo {
    /**
     * <p>The regex string to match on the message. Based on SPeL with the {@link OutgoingWebhookPayload#getText()}
     * as the root object.</p>
     * e.g.
     * <code>#root contains '\\b(Hi|Hello|Howdy)'</code>
     * <p>This would match anything sent directly to the trigger word with the word Hi or Hello in it.</p>
     *
     * @return The Regex
     */
    String regex();

    /**
     * <p>The regex string to match on the channel. Based on SPeL with the {@link OutgoingWebhookPayload#getChannelName()} as
     * the root object.</p>
     * e.g.
     * <code>#root matches 'Test'</code>
     * <p>This would match anything sent to the Test room.</p>
     * <p>This does have to be one of the rooms that echo is listening to.</p>
     *
     * @return The Regex
     */
    String channel() default "";
}
