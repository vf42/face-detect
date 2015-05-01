package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Second two-rect feature.
 * @author fedorovvadim
 *
 */
public class TwoRectVerticalHLF extends HaarLikeFeature {

    private final int halfsize;

    public TwoRectVerticalHLF(int size) {
        super(size);
        this.halfsize = size / 2;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, size, halfsize);
        final double val2 = ii.getRectValue(x, y + halfsize, size, halfsize);
        return val1 - val2;
    }

}
