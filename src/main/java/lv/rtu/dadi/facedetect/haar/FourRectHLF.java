package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Four-rect Haar-like feature.
 * @author fedorovvadim
 *
 */
public class FourRectHLF extends HaarLikeFeature {

    private final int width_2;
    private final int height_2;

    public FourRectHLF(int width, int height) {
        super(width, height);
        this.width_2 = width / 2;
        this.height_2 = height / 2;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, width_2, height_2);
        final double val2 = ii.getRectValue(x + width_2, y, width_2, height_2);
        final double val3 = ii.getRectValue(x, y + height_2, width_2, height_2);
        final double val4 = ii.getRectValue(x + width_2, y + height_2, width_2, height_2);
        return val2 + val3 - val1 - val4;
    }

}
