package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.Hear;
import org.homonoia.echo.bot.annotations.OnJoin;
import org.homonoia.echo.bot.annotations.OnLeave;
import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.RoomEnter;
import org.homonoia.echo.model.RoomEvent;
import org.homonoia.echo.model.RoomExit;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.RoomNotification;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

import static java.util.Objects.nonNull;

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
            return evaluate(regex, room, roomExit, selfPassed, roomExit.getSender(), roomExit.getRoom());
        }
        return false;
    }
}
