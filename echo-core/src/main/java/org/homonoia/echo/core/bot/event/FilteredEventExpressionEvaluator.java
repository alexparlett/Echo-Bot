package org.homonoia.echo.core.bot.event;

import org.homonoia.echo.core.bot.event.parser.SpelExpressionParser;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class FilteredEventExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<CachedExpressionEvaluator.ExpressionKey, Expression>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<AnnotatedElementKey, Method>(64);


    public FilteredEventExpressionEvaluator() {
        super(new SpelExpressionParser());
    }

    /**
     * Create the suitable {@link EvaluationContext} for the specified event handling
     * on the specified method.
     */
    public EvaluationContext createEvaluationContext(Object event, Class<?> targetClass,
                                                     Method method, Object[] args, BeanFactory beanFactory) {

        Method targetMethod = getTargetMethod(targetClass, method);
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(
                event, targetMethod, args, getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    /**
     * Specify if the condition defined by the specified expression matches.
     */
    public boolean condition(String conditionExpression,
                             AnnotatedElementKey elementKey, EvaluationContext evalContext) {

        return getExpression(this.conditionCache, elementKey, conditionExpression)
                .getValue(evalContext, boolean.class);
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            if (targetMethod == null) {
                targetMethod = method;
            }
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}
