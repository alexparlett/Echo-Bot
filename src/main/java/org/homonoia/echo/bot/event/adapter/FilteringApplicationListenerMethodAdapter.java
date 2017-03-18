package org.homonoia.echo.bot.event.adapter;

import org.homonoia.echo.bot.event.FilteredEventExpressionEvaluator;
import org.homonoia.echo.configuration.properties.HipchatProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class FilteringApplicationListenerMethodAdapter extends ApplicationListenerMethodAdapter {
    protected final FilteredEventExpressionEvaluator evaluator;
    protected final ApplicationContext applicationContext;
    protected final Class<?> targetClass;
    protected final AnnotatedElementKey methodKey;
    protected final Method method;
    protected final HipchatProperties hipchatProperties;

    public FilteringApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method,
                                                     FilteredEventExpressionEvaluator evaluator, ApplicationContext applicationContext,
                                                     HipchatProperties hipchatProperties) {
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

    protected boolean shouldHandle(ApplicationEvent event, Object[] args) {
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

    protected boolean filterEvent(Object arg) {
        return true;
    }

    protected boolean evaluate(String condition, Object root) {
        if (StringUtils.hasText(condition)) {
            Assert.notNull(this.evaluator, "EventExpressionEvaluator must no be null");
            EvaluationContext evaluationContext = this.evaluator.createEvaluationContext(
                    root, this.targetClass, this.method, new Object[]{root}, this.applicationContext);

            return this.evaluator.condition(condition, this.methodKey, evaluationContext);
        }
        return true;
    }
}
