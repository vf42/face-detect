package lv.rtu.dadi.facedetect.haar;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;



/**
 * Abstract class that provides universal tools for Haar-like feature computation.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class HaarLikeFeature {

    public final HaarRectangle[] rects;
    public final HaarRectangle fullArea;

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
        this.fullArea = new HaarRectangle(0, 0, width, height, 1.0);
    }

    public HaarLikeFeature(HaarRectangle[] rects, int width, int height) {
        this.rects = rects;
        this.width = width;
        this.height = height;
        this.fullArea = new HaarRectangle(0, 0, width, height, 1.0);
//        this.fullArea = new HaarRectangle(
//                Stream.of(rects).mapToInt(r -> r.x0).min().getAsInt(),
//                Stream.of(rects).mapToInt(r -> r.y0).min().getAsInt(),
//                Stream.of(rects).mapToInt(r -> r.x1).max().getAsInt(),
//                Stream.of(rects).mapToInt(r -> r.y1).max().getAsInt(), 1.0);
    }

    /**
     * Calculate the feature value for a single position.
     * @param ii
     * @param x
     * @param y
     * @return
     */
    public double getFeatureValue(IntegralImage ii, int x, int y) {
        return getFeatureValue(ii, x, y, 1.0);
    }

    public double getFeatureValue(IntegralImage ii, int x, int y, double scale) {
        // Get normalization coefficient for full area of the window.
        final double invArea = 1.0 / (width * scale * height * scale);
        final double fullValue = ii.getRectValue(x, y, fullArea.scaled(scale));
        final double fullSqValue = ii.getSqRectValue(x, y, fullArea.scaled(scale));
        final double moy = fullValue * invArea;
        final double rnorm = fullSqValue * invArea - moy * moy;
        final double norm = rnorm > 1.0 ? Math.sqrt(rnorm) : 1.0;

        double rectSum = 0;
        for (final HaarRectangle r : rects) {
            rectSum += r.weight * ii.getRectValue(x, y, r.scaled(scale));
        }
        final double rectSum2 = rectSum * invArea;
        return rectSum2 / norm;
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
