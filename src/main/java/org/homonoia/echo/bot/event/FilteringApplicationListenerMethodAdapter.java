package org.homonoia.echo.bot.event;

import org.homonoia.echo.bot.annotations.Hear;
import org.homonoia.echo.bot.annotations.OnJoin;
import org.homonoia.echo.bot.annotations.OnLeave;
import org.homonoia.echo.bot.annotations.RespondTo;
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
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class FilteringApplicationListenerMethodAdapter extends ApplicationListenerMethodAdapter {
    private final FilteredEventExpressionEvaluator evaluator;
    private final ApplicationContext applicationContext;
    private final Class<?> targetClass;
    private final AnnotatedElementKey methodKey;
    private final Method method;
    private final HipchatProperties hipchatProperties;

    public FilteringApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method,
                                                     FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext, HipchatProperties hipchatProperties) {
        super(beanName, targetClass, method);
        this.evaluator = evaluator;
        this.applicationContext = applicationContext;
        this.targetClass = targetClass;
        this.methodKey = new AnnotatedElementKey(method, targetClass);
        this.method = method;
        this.hipchatProperties = hipchatProperties;
    }

    @Override
    public void processEvent(ApplicationEvent event) {
        Object[] args = resolveArguments(event);
        if (shouldHandle(event, args)) {
            Object result = doInvoke(args);
            if (result != null) {
                handleResult(result);
            } else {
                logger.trace("No result object given - no result to handle");
            }
        }
    }

    private boolean shouldHandle(ApplicationEvent event, Object[] args) {
        if (args == null) {
            return false;
        }
        String condition = getCondition();
        if (StringUtils.hasText(condition)) {
            Assert.notNull(this.evaluator, "EventExpressionEvaluator must no be null");
            EvaluationContext evaluationContext = this.evaluator.createEvaluationContext(
                    event, this.targetClass, this.method, args, this.applicationContext);
            boolean conditionPassed = this.evaluator.condition(condition, this.methodKey, evaluationContext);
            return conditionPassed && filterEvent(args[0]);
        }
        return filterEvent(args[0]);
    }

    private boolean filterEvent(Object arg) {
        if (arg instanceof RoomMessage) {
            String regex;
            boolean self;
            String room;
            RespondTo respondTo = AnnotationUtils.getAnnotation(method, RespondTo.class);
            if (nonNull(respondTo)) {
                regex = respondTo.regex();
                self = respondTo.self();
                room = respondTo.room();
            } else {
                Hear hear = AnnotationUtils.getAnnotation(method, Hear.class);
                regex = hear.regex();
                self = hear.self();
                room = hear.room();
            }

            RoomMessage roomMessage = (RoomMessage) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomMessage.getMessage().getFrom().getMentionName(), hipchatProperties.getMentionName());
            }

            return evaluate(regex, room, roomMessage, selfPassed);
        } else if (arg instanceof RoomNotification) {
            String regex;
            boolean self;
            String room;
            RespondTo respondTo = AnnotationUtils.getAnnotation(method, RespondTo.class);
            if (nonNull(respondTo)) {
                regex = respondTo.regex();
                self = respondTo.self();
                room = respondTo.room();
            } else {
                Hear hear = AnnotationUtils.getAnnotation(method, Hear.class);
                regex = hear.regex();
                self = hear.self();
                room = hear.room();
            }

            RoomNotification roomNotification = (RoomNotification) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomNotification.getMessage().getFrom(), hipchatProperties.getMentionName());
            }

            return evaluate(regex, room, roomNotification, selfPassed);
        } else if (arg instanceof RoomEnter) {
            OnJoin ann = AnnotationUtils.getAnnotation(method, OnJoin.class);
            String regex = ann.regex();
            boolean self = ann.self();
            String room = ann.room();

            RoomEnter roomEnter = (RoomEnter) arg;

            boolean selfPassed = true;
            if (self) {
                selfPassed = !Objects.equals(roomEnter.getSender().getMentionName(), hipchatProperties.getMentionName());
            }
            return evaluate(regex, room, roomEnter, selfPassed);
        } else if (arg instanceof RoomExit) {
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
        return true;
    }

    private boolean evaluate(String regex, String room, RoomEvent event, boolean selfPassed) {
        if (StringUtils.hasText(room) || StringUtils.hasText(regex)) {
            boolean regexPassed = true;
            boolean roomPassed = true;
            Assert.notNull(this.evaluator, "EventExpressionEvaluator must no be null");
            EvaluationContext evaluationContext = this.evaluator.createEvaluationContext(
                    event, this.targetClass, this.method, new Object[]{event}, this.applicationContext);

            if (StringUtils.hasText(regex)) {
                regexPassed = this.evaluator.condition(regex, this.methodKey, evaluationContext);
            }

            if (StringUtils.hasText(room)) {
                roomPassed = this.evaluator.condition(room, this.methodKey, evaluationContext);
            }

            return selfPassed && roomPassed && regexPassed;
        }
        return selfPassed;
    }
}
