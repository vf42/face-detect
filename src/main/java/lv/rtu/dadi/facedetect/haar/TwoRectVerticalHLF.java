package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Second two-rect feature.
 * @author fedorovvadim
 *
 */
public class TwoRectVerticalHLF extends HaarLikeFeature {

    private final int height_2;

    public TwoRectVerticalHLF(int width, int height) {
        super(width, height);
        this.height_2 = height / 2;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, width, height_2);
        final double val2 = ii.getRectValue(x, y + height_2, width, height_2);
        return val1 - val2;
    }

}
