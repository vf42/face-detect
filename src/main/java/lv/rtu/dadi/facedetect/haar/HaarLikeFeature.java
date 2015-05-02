package lv.rtu.dadi.facedetect.haar;


/**
 * Abstract class that provides universal tools for Haar-like feature computation.
 * @author fedorovvadim
 *
 */
public class HaarLikeFeature {

    public final HaarRectangle[] rects;

    public final int width;
    public final int height;

    public HaarLikeFeature(HaarRectangle[] rects) {
        this.rects = rects;
        // Calculate width and height values from rects.
        int width = 0;
        int height = 0;
        for (final HaarRectangle r : rects) {
            if (r.x1 > width) width = r.x1;
            if (r.y1 > height) height = r.y1;
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
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        double value = 0.0;
        for (final HaarRectangle r : rects) {
            value += r.weight * ii.getRectValue(x, y, r);
        }
        return value;
    }

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
