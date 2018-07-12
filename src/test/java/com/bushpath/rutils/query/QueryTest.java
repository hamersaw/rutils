package com.bushpath.rutils.query;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryTest {
    @Test
    public void evaluationTest() {
        // EqualExpression
        EqualExpression<Double> equalExpression = new EqualExpression(0.0);
        assertEquals(false, equalExpression.evaluate(-1.0));
        assertEquals(true, equalExpression.evaluate(0.0));
        assertEquals(false, equalExpression.evaluate(1.0));

        // GreaterExpression
        GreaterExpression<Double> greaterExpression = new GreaterExpression(0.0);
        assertEquals(false, greaterExpression.evaluate(-1.0));
        assertEquals(false, greaterExpression.evaluate(0.0));
        assertEquals(true, greaterExpression.evaluate(1.0));
 
        // GreaterEqualExpression
        GreaterEqualExpression<Double> greaterEqualExpression =
            new GreaterEqualExpression(0.0);
        assertEquals(false, greaterEqualExpression.evaluate(-1.0));
        assertEquals(true, greaterEqualExpression.evaluate(0.0));
        assertEquals(true, greaterEqualExpression.evaluate(1.0));

        // LessExpression
        LessExpression<Double> lessExpression = new LessExpression(0.0);
        assertEquals(true, lessExpression.evaluate(-1.0));
        assertEquals(false, lessExpression.evaluate(0.0));
        assertEquals(false, lessExpression.evaluate(1.0));

        // LessEqualExpression
        LessEqualExpression<Double> lessEqualExpression = new LessEqualExpression(0.0);
        assertEquals(true, lessEqualExpression.evaluate(-1.0));
        assertEquals(true, lessEqualExpression.evaluate(0.0));
        assertEquals(false, lessEqualExpression.evaluate(1.0));
    }
}
