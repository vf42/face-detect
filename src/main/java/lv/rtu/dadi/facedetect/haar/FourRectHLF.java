package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Four-rect Haar-like feature.
 * @author fedorovvadim
 *
 */
public class FourRectHLF extends HaarLikeFeature {

    private final int halfsize;

    public FourRectHLF(int size) {
        super(size);
        this.halfsize = size / 2;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, halfsize, halfsize);
        final double val2 = ii.getRectValue(x + halfsize, y, halfsize, halfsize);
        final double val3 = ii.getRectValue(x, y + halfsize, halfsize, halfsize);
        final double val4 = ii.getRectValue(x + halfsize, y + halfsize, halfsize, halfsize);
        return val2 + val3 - val1 - val4;
    }

}
