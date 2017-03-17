package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.OnJoin;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.RoomEnter;
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
public class OnJoinApplicationListenerMethodAdapter extends FilteringApplicationListenerMethodAdapter {

    public OnJoinApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method, FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext, HipchatProperties hipchatProperties) {
        super(beanName, targetClass, method, evaluator, applicationContext, hipchatProperties);
    }

    @Override
    protected boolean filterEvent(Object arg) {
        if (arg instanceof RoomEnter) {
            OnJoin ann = AnnotationUtils.getAnnotation(method, OnJoin.class);
            String regex = ann.regex();
            boolean self = ann.self();
            String room = ann.room();

            RoomEnter roomEnter = (RoomEnter) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomEnter.getSender().getMentionName(), hipchatProperties.getMentionName());
            }
            return selfPassed && evaluate(regex, roomEnter.getSender()) && evaluate(room, roomEnter.getRoom());
        }
        return false;
    }
}
