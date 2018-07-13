package com.bushpath.rutils.query;

import com.bushpath.rutils.query.parser.FeatureRangeParser;
import com.bushpath.rutils.query.parser.Parser;

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

        // AndExpression
        AndExpression<Double> andExpression =
            new AndExpression(greaterEqualExpression, lessEqualExpression);
        assertEquals(false, andExpression.evaluate(-1.0));
        assertEquals(true, andExpression.evaluate(0.0));
        assertEquals(false, andExpression.evaluate(1.0));

        // OrExpression
        OrExpression<Double> orExpression =
            new OrExpression(greaterExpression, lessExpression);
        assertEquals(true, orExpression.evaluate(-1.0));
        assertEquals(false, orExpression.evaluate(0.0));
        assertEquals(true, orExpression.evaluate(1.0));
    }

    @Test
    public void featureRangeParserTest() {
        try {
            Parser parser = new FeatureRangeParser();
            Query query = parser.evaluate("f0:0..10", "f1:..10", "f2:0..");

            assertEquals(true, query.getExpression("f0").evaluate(10.0f));
            assertEquals(true, query.getExpression("f0").evaluate(0.0f));
            assertEquals(false, query.getExpression("f0").evaluate(11.0f));
            assertEquals(false, query.getExpression("f0").evaluate(-1.0f));

            assertEquals(true, query.getExpression("f1").evaluate(10.0f));
            assertEquals(true, query.getExpression("f1").evaluate(0.0f));
            assertEquals(false, query.getExpression("f1").evaluate(11.0f));
            assertEquals(true, query.getExpression("f1").evaluate(-1.0f));

            assertEquals(true, query.getExpression("f2").evaluate(10.0f));
            assertEquals(true, query.getExpression("f2").evaluate(0.0f));
            assertEquals(true, query.getExpression("f2").evaluate(11.0f));
            assertEquals(false, query.getExpression("f2").evaluate(-1.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
