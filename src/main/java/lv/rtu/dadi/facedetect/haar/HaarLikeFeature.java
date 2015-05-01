package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Abstract class that provides universal tools for Haar-like feature computation.
 * @author fedorovvadim
 *
 */
public abstract class HaarLikeFeature {

    public final int size; // Assuming square-sized features.

    public HaarLikeFeature(int size) {
        if (size % 2 != 0) {
            throw new RuntimeException("Feature size must be multiple of 2!");
        }
        this.size = size;
    }

    /**
     * Calculate the feature value for a single position.
     * @param ii
     * @param x
     * @param y
     * @return
     */
    public abstract double getFeatureValue(IntegralImage ii, int x, int y);

    /**
     * Scan the feature on specified region of the image.
     * @param ii
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public double[][] regionScan(IntegralImage ii, int x, int y, int w, int h) {
        final double[][] result = new double[w - size][h - size];
        for (int i = 0; i < w - size; i++) {
            for (int j = 0; j < h - size; j++) {
                if (x + i + size < ii.getWidth() && y + j + size < ii.getHeight()) {
                    result[i][j] = getFeatureValue(ii, x + i, y + j);
                }
            }
        }
        return result;
    }

}
