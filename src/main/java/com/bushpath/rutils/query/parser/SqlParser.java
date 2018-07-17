package com.bushpath.rutils.query.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.bushpath.rutils.query.AndExpression;
import com.bushpath.rutils.query.EqualExpression;
import com.bushpath.rutils.query.Expression;
import com.bushpath.rutils.query.GreaterEqualExpression;
import com.bushpath.rutils.query.GreaterExpression;
import com.bushpath.rutils.query.LessEqualExpression;
import com.bushpath.rutils.query.LessExpression;
import com.bushpath.rutils.query.Query;
import com.bushpath.rutils.query.parser.antlr.SqlLexer;
import com.bushpath.rutils.query.parser.antlr.SqlBaseListener;
//import com.bushpath.rutils.query.parser.antlr.SqlParser;
//
import java.util.Arrays;
import java.util.HashMap;

public class SqlParser extends Parser {
    public Query evaluate(String... arguments) throws Exception {
        // validate arguments
        if (arguments.length != 1) {
            throw new
                IllegalArgumentException("SqlParser only accepts one string argument");
        }

        // lex and parse
        SqlLexer sqlLexer = new SqlLexer(new ANTLRInputStream(arguments[0]));
        CommonTokenStream tokens = new CommonTokenStream(sqlLexer);
        com.bushpath.rutils.query.parser.antlr.SqlParser sqlParser =
            new com.bushpath.rutils.query.parser.antlr.SqlParser(tokens);

        sqlParser.addErrorListener(
            new BaseErrorListener() {
                @Override
				public void syntaxError(Recognizer<?, ?> recognizer, 
                        Object offendingSymbol, int line, int charPositionInLine, 
                        String msg, RecognitionException e) {
                    throw new IllegalStateException("failed to parse at line "
                        + line + " due to " + msg, e);
                }
            });

        // walk over parsed tokens and create query
        Listener listener = new Listener();
        sqlParser.addParseListener(listener);
        sqlParser.select();

        return new Query(listener.entity, listener.expressions);
    }

    protected class Listener extends SqlBaseListener {
        public String entity;
        public HashMap<String, Expression> expressions;

        public Listener() {
            this.expressions = new HashMap();
        }

        @Override
        public void exitSelect(
                com.bushpath.rutils.query.parser.antlr.SqlParser.SelectContext ctx) {
            this.entity = ctx.entity.getText();
        }

        @Override
        public void exitExpr(
                com.bushpath.rutils.query.parser.antlr.SqlParser.ExprContext ctx) {
            // parse and create expression
            Expression<Float> expression = null;
            String operand = ctx.opr.getText();
            Float value = Float.parseFloat(ctx.value.getText());
            if (operand.equals("<")) {
                expression = new LessExpression(value);
            } else if (operand.equals("<=")) {
                expression = new LessEqualExpression(value);
            } else if (operand.equals("=")) {
                expression = new EqualExpression(value);
            } else if (operand.equals(">")) {
                expression = new GreaterExpression(value);
            } else if (operand.equals(">=")) {
                expression = new GreaterEqualExpression(value);
            }

            // add to existing expressions
            String feature = ctx.feature.getText();
            if (this.expressions.containsKey(feature)) {
                Expression e2 = this.expressions.get(feature);
                if (e2 instanceof AndExpression) {
                    ((AndExpression) e2).addExpression(expression);
                } else {
                    AndExpression andExpression = new AndExpression();
                    andExpression.addExpression(expression);
                    andExpression.addExpression(e2);

                    this.expressions.put(feature, andExpression);
                }
            } else {
                this.expressions.put(feature, expression);
            }
        }
    }
}
