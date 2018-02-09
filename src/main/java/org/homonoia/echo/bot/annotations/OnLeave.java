package org.homonoia.echo.bot.annotations;

import org.homonoia.echo.model.RoomExit;
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
@EventListener(classes = {RoomExit.class})
@Async
public @interface OnLeave {
    /**
     * Whether Echo will respond to itself leaving a room
     *
     * @return true | false
     */
    boolean self() default false;

    /**
     * <p>The regex string to match on the user leaving. Based on SPeL with the {@link org.homonoia.echo.model.User} as
     * the root object.</p>
     * e.g.
     * <code>#root.mentionName matches '(Echo|Mark|Larry)'</code>
     * <p>This would match Echo, Mark or Larry leaving.</p>
     *
     * @return The Regex
     */
    String regex() default "";

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
