package lv.rtu.dadi.facedetect.haar;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HaarRectangleTest {

    public HaarRectangleTest() {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testFullAreaScaling() {
        final HaarRectangle r1 = new HaarRectangle(0, 0, 24, 24, 1.0);
        final HaarRectangle r2 = r1.scaled(1);
        assertTrue(24.0 == r2.x1);
        assertTrue(24.0 == r2.y1);
        assertTrue(0.0 == r2.x0);

        final HaarRectangle r3 = r1.scaled(0.5);
        assertTrue(12.0 == r3.x1);
        assertTrue(12.0 == r3.y1);
        assertTrue(0.0 == r3.x0);

        final HaarRectangle r4 = r1.scaled(1.5);
        assertTrue(36.0 == r4.x1);
        assertTrue(36.0 == r4.y1);
        assertTrue(0.0 == r4.x0);
    }

    @Test
    public void testSubrectScaling() {
        final HaarRectangle r1 = new HaarRectangle(4, 6, 12, 18, 1.0);
        final HaarRectangle r2 = r1.scaled(1);
        assertTrue(4 == r2.x0);
        assertTrue(6 == r2.y0);
        assertTrue(12 == r2.x1);
        assertTrue(18 == r2.y1);

        final HaarRectangle r3 = r1.scaled(0.5);
        assertTrue(2 == r3.x0);
        assertTrue(3 == r3.y0);
        assertTrue(6 == r3.x1);
        assertTrue(9 == r3.y1);

        final HaarRectangle r4 = r1.scaled(1.5);
        assertTrue(6 == r4.x0);
        assertTrue(9 == r4.y0);
        assertTrue(18 == r4.x1);
        assertTrue(27 == r4.y1);
    }

}
