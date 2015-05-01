package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Three-rect feature.
 * @author fedorovvadim
 *
 */
public class ThreeRectHorizontalHLF extends HaarLikeFeature {

    public final int size_3;

    public ThreeRectHorizontalHLF(int size) {
        super(size);
        if (size % 3 != 0) {
            throw new RuntimeException("Size must be multiple of 3!");
        }
        size_3 = size / 3;
    }

    @Override
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        final double val1 = ii.getRectValue(x, y, size_3, size);
        final double val2 = ii.getRectValue(x + size_3, y, size_3, size);
        final double val3 = ii.getRectValue(x + 2*size_3, y, size_3, size);
        return val2 - val1 - val3;
    }

}
