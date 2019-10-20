package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.bot.event.MattermostEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class RespondToApplicationListenerMethodAdapter extends FilteringApplicationListenerMethodAdapter {

    public RespondToApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method,
                                                     FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext) {
        super(beanName, targetClass, method, evaluator, applicationContext);
    }

    @Override
    protected boolean filterEvent(Object arg) {
        if (arg instanceof MattermostEvent) {
            RespondTo respondTo = AnnotationUtils.getAnnotation(method, RespondTo.class);
            String regex = respondTo.regex();
            String channel = respondTo.channel();

            MattermostEvent event = (MattermostEvent) arg;

            return  evaluate(regex, event.getPayload().getText()) && evaluate(channel, event.getPayload().getChannelName());
        }
        return false;
    }
}
