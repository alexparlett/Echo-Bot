package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.OnLeave;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.RoomEnter;
import org.homonoia.echo.model.RoomExit;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class OnLeaveApplicationListenerMethodAdapter extends FilteringApplicationListenerMethodAdapter {

    public OnLeaveApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method, FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext, HipchatProperties hipchatProperties) {
        super(beanName, targetClass, method, evaluator, applicationContext, hipchatProperties);
    }

    @Override
    protected boolean filterEvent(Object arg) {
        if (arg instanceof RoomEnter) {
            OnLeave ann = AnnotationUtils.getAnnotation(method, OnLeave.class);
            String regex = ann.regex();
            boolean self = ann.self();
            String room = ann.room();

            RoomExit roomExit = (RoomExit) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomExit.getSender().getMentionName(), hipchatProperties.getMentionName());
            }
            return evaluate(regex, room, roomExit, selfPassed);
        }
        return false;
    }
}
