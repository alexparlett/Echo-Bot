package org.homonoia.echo.bot.annotations;

import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.RoomNotification;
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
@EventListener(classes = {RoomMessage.class, RoomNotification.class})
@Async
public @interface Hear {
    /**
     * Whether Echo will respond to messages from itself
     *
     * @return true | false
     */
    boolean self() default false;

    /**
     * <p>The regex string to match on the message. Based on SPeL with the {@link org.homonoia.echo.model.Message} or
     * {@link org.homonoia.echo.model.Notification} as the root object.</p>
     * e.g.
     * <code>#root.message matches '(Hi|Hello|Howdy).?Echo'</code>
     * <p>This would match anything said with the word Hi, Howdy or Hello followed by Echo in it.</p>
     *
     * @return The Regex
     */
    String regex();

    /**
     * <p>The regex string to match on the room. Based on SPeL with the {@link org.homonoia.echo.model.Room} as
     * the root object.</p>
     * e.g.
     * <code>#root.name matches 'Test'</code>
     * <p>This would match anything sent to the Test room.</p>
     * <p>This does have to be one of the rooms that echo is listening to.</p>
     *
     * @return The Regex
     */
    String room() default "";
}
