package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * First two-rect feature.
 * @author fedorovvadim
 *
 */
public final class TwoRectHorizontalHLF extends HaarLikeFeature {

    private final int width_2;

    public TwoRectHorizontalHLF(int width, int height) {
        super(width, height);
        this.width_2 = width / 2;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, width_2, height);
        final double val2 = ii.getRectValue(x + width_2, y, width_2, height);
        return val2 - val1;
    }

}
