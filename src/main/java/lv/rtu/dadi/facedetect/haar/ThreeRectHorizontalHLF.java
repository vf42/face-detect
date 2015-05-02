package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Three-rect feature.
 * @author fedorovvadim
 *
 */
public class ThreeRectHorizontalHLF extends HaarLikeFeature {

    public final int width_3;

    public ThreeRectHorizontalHLF(int width, int height) {
        super(width, height);
        if (width % 3 != 0) {
            throw new RuntimeException("Width must be multiple of 3!");
        }
        width_3 = width / 3;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, width_3, height);
        final double val2 = ii.getRectValue(x + width_3, y, width_3, height);
        final double val3 = ii.getRectValue(x + 2*width_3, y, width_3, height);
        return val2 - val1 - val3;
    }

}
