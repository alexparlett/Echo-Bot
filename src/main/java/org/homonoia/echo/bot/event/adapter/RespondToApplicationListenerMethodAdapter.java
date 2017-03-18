package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.RoomNotification;
import org.homonoia.echo.model.User;
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

    public RespondToApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method,
                                                   FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext,
                                                   User user) {
        super(beanName, targetClass, method, evaluator, applicationContext, user);
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
                selfPassed = !Objects.equals(roomMessage.getMessage().getFrom().getMentionName(), user.getMentionName());
            }

            return selfPassed && evaluate(regex, roomMessage.getMessage()) && evaluate(room, roomMessage.getRoom());
        } else if (arg instanceof RoomNotification) {
            RespondTo respondTo = AnnotationUtils.getAnnotation(method, RespondTo.class);
            String regex = respondTo.regex();
            boolean self = respondTo.self();
            String room = respondTo.room();

            RoomNotification roomNotification = (RoomNotification) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomNotification.getMessage().getFrom(), user.getName());
            }

            return selfPassed && evaluate(regex, roomNotification.getMessage()) && evaluate(room, roomNotification.getRoom());
        }
        return false;
    }
}
