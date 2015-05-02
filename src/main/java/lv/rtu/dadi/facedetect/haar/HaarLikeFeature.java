package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;

/**
 * Abstract class that provides universal tools for Haar-like feature computation.
 * @author fedorovvadim
 *
 */
public abstract class HaarLikeFeature {

    public final int width;
    public final int height;

    public HaarLikeFeature(int width, int height) {
        if (width % 2 != 0 && height % 2 != 0) {
            throw new RuntimeException("Feature size must be multiple of 2!");
        }
        this.width = width;
        this.height = height;
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
        final double[][] result = new double[w - width][h - height];
        for (int i = 0; i < w - width; i++) {
            for (int j = 0; j < h - height; j++) {
                if (x + i + width < ii.getWidth() && y + j + height < ii.getHeight()) {
                    result[i][j] = getFeatureValue(ii, x + i, y + j);
                }
            }
        }
        return result;
    }

}
