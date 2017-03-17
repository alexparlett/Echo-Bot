package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.RespondTo;
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
public class RespondToApplicationListenerMethodAdapter extends FilteringApplicationListenerMethodAdapter {

    public RespondToApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method, FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext, HipchatProperties hipchatProperties) {
        super(beanName, targetClass, method, evaluator, applicationContext, hipchatProperties);
    }

    @Override
    protected boolean filterEvent(Object arg) {
        if (arg instanceof RoomMessage) {
            RespondTo respondTo = AnnotationUtils.getAnnotation(method, RespondTo.class);
            String regex = respondTo.regex();
            boolean self = respondTo.self();
            String room = respondTo.room();

            RoomMessage roomMessage = (RoomMessage) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomMessage.getMessage().getFrom().getMentionName(), hipchatProperties.getMentionName());
            }

            return evaluate(regex, room, roomMessage, selfPassed, roomMessage.getMessage(), roomMessage.getRoom());
        } else if (arg instanceof RoomNotification) {
            RespondTo respondTo = AnnotationUtils.getAnnotation(method, RespondTo.class);
            String regex = respondTo.regex();
            boolean self = respondTo.self();
            String room = respondTo.room();

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
