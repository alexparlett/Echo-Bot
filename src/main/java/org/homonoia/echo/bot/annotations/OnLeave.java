package org.homonoia.echo.bot.annotations;

import org.homonoia.echo.model.RoomExit;
import org.springframework.context.event.EventListener;

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
public @interface OnLeave {
    boolean self() default false;
    String regex() default "";
    String room() default "";
}
