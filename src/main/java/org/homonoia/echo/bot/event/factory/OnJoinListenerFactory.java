package org.homonoia.echo.bot.event.factory;

import org.homonoia.echo.bot.annotations.OnJoin;
import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.bot.event.adapter.OnJoinApplicationListenerMethodAdapter;
import org.homonoia.echo.model.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class OnJoinListenerFactory implements EventListenerFactory, Ordered, ApplicationContextAware {

    private final User user;
    private ApplicationContext applicationContext;
    private final FilteredEventExpressionEvaluator evaluator = new FilteredEventExpressionEvaluator();

    public OnJoinListenerFactory(User user) {
        this.user = user;
    }

    @Override
    public boolean supportsMethod(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, OnJoin.class);
    }

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new OnJoinApplicationListenerMethodAdapter(beanName, type, method,evaluator,applicationContext,user);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
