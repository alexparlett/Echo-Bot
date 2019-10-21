package org.homonoia.echo.core.documentation;

import lombok.Getter;
import org.homonoia.echo.core.documentation.annotations.EchoDoc;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

/**
 * Copyright (c) 2015-2018 Homonoia Studios.
 *
 * @author alexparlett
 * @since 11/11/2018
 */
@Getter
@Component
public class DocumentationProcessor implements BeanPostProcessor {

    private MultiValueMap<String, EchoDoc> echoDocumentation = new LinkedMultiValueMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Stream.of(bean.getClass().getMethods())
                .filter(method -> AnnotatedElementUtils.hasAnnotation(method, EchoDoc.class))
                .map(method -> AnnotationUtils.findAnnotation(method, EchoDoc.class))
                .forEach(echoDoc -> echoDocumentation.add(echoDoc.namespace(), echoDoc));

        return bean;
    }
}
