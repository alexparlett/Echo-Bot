package org.homonoia.echo.bot.event.parser;

import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpression;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class SpelExpressionParser extends org.springframework.expression.spel.standard.SpelExpressionParser {

    private final SpelParserConfiguration configuration;

    /**
     * Create a parser with default settings.
     */
    public SpelExpressionParser() {
        this.configuration = new SpelParserConfiguration();
    }

    @Override
    protected SpelExpression doParseExpression(String expressionString, ParserContext context) throws ParseException {
        return new InternalSpelExpressionParser(this.configuration).doParseExpression(expressionString, context);
    }
}
