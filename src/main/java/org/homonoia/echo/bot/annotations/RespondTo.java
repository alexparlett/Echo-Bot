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
@EventListener(condition = "#a0.self == true", classes = {RoomMessage.class, RoomNotification.class})
@Async
public @interface RespondTo {
    boolean self() default false;
    String regex() default "";
    String room() default "";
}
