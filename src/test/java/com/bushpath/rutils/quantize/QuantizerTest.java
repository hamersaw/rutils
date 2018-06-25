package com.bushpath.rutils.quantize;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuantizerTest {
    @Test
    public void binCheck() {
        Quantizer quantizer = new Quantizer(new double[]{0,2,4,6,8,10});
        try {
            assertEquals(quantizer.evaluate(0), 0);
            assertEquals(quantizer.evaluate(1), 0);
            assertEquals(quantizer.evaluate(2), 1);
            assertEquals(quantizer.evaluate(3), 1);
            assertEquals(quantizer.evaluate(4), 2);
            assertEquals(quantizer.evaluate(5), 2);
            assertEquals(quantizer.evaluate(6), 3);
            assertEquals(quantizer.evaluate(7), 3);
            assertEquals(quantizer.evaluate(8), 4);
            assertEquals(quantizer.evaluate(9), 4);
            assertEquals(quantizer.evaluate(10), 4);
        } catch (Exception e) {
            
        }
    }
}
