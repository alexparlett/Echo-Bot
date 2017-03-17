package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.Hear;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.RoomNotification;
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
public class HearApplicationListenerMethodAdapter extends FilteringApplicationListenerMethodAdapter {

    public HearApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method, FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext, HipchatProperties hipchatProperties) {
        super(beanName, targetClass, method, evaluator, applicationContext, hipchatProperties);
    }

    @Override
    protected boolean filterEvent(Object arg) {
        if (arg instanceof RoomMessage) {
            Hear hear = AnnotationUtils.getAnnotation(method, Hear.class);
            String regex = hear.regex();
            boolean self = hear.self();
            String room = hear.room();

            RoomMessage roomMessage = (RoomMessage) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomMessage.getMessage().getFrom().getMentionName(), hipchatProperties.getMentionName());
            }

            return evaluate(regex, room, roomMessage, selfPassed);
        } else if (arg instanceof RoomNotification) {
            Hear hear = AnnotationUtils.getAnnotation(method, Hear.class);
            String regex = hear.regex();
            boolean self = hear.self();
            String room = hear.room();

            RoomNotification roomNotification = (RoomNotification) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomNotification.getMessage().getFrom(), hipchatProperties.getMentionName());
            }

            return evaluate(regex, room, roomNotification, selfPassed, roomNotification.getMessage(), roomNotification.getRoom());
        }
        return false;
    }
}
