package lv.rtu.dadi.facedetect.filters;

import java.util.Arrays;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;

/**
 * Creates linear filter for an arbitrary NxN filter.
 * @author fedorovvadim
 *
 */
public final class GrayscaleFilterFactory {

    /**
     * Low frequency.
     */
    public static final short[][] LOWFREQ_3X3_1 =
        {{1, 1, 1},
         {1, 1, 1},
         {1, 1, 1}};

    public static final short[][] LOWFREQ_3X3_2 =
        {{1, 1, 1},
         {1, 2, 1},
         {1, 1, 1}};

    public static final short[][] LOWFREQ_3X3_3 =
        {{1, 2, 1},
         {2, 4, 2},
         {1, 2, 1}};

    /**
     * High frequency.
     */
    public static final short[][] HIFREQ_3X3_1 =
        {{ 0, -1,  0},
         {-1,  5, -1},
         { 0, -1,  0}};

    public static final short[][] HIFREQ_3X3_2 =
        {{-1, -1, -1},
         {-1,  9, -1},
         {-1, -1, -1}};

    public static final short[][] HIFREQ_3X3_3 =
        {{ 0, -2,  0},
         {-2,  5, -2},
         { 0, -2,  0}};

    /*
     * Prewitt and Sobel filters for edge detection.
     */
    public static final short[][] PREWITT_GX =
        {{-1,  0,  1},
         {-1,  0,  1},
         {-1,  0,  1}};
    public static final short[][] PREWITT_GY =
        {{-1, -1, -1},
         { 0,  0,  0},
         { 1,  1,  1}};

    public static final short[][] SOBEL_GX =
        {{-1,  0,  1},
         {-2,  0,  2},
         {-1,  0,  1}};
    public static final short[][] SOBEL_GY =
        {{-1, -2, -1},
         { 0,  0,  0},
         { 1,  2,  1}};

    /**
     * Return linear filter instance.
     * @param _filter
     * @return
     */
    public static GrayscaleFilter getLinearFilter(final short[][] _filter) {
        return new GrayscaleFilter() {
            short[][] filter = _filter;
            @Override
            public GrayscaleImage apply(GrayscaleImage source) {
                // Filter should be odd square array.
                if (filter.length % 2 != 1 || filter.length != filter[0].length) {
                    throw new RuntimeException("Only square odd filters are allowed.");
                }
                final GrayscaleImage result = new GrayscaleImage(source.getWidth(), source.getHeight());
                final int size = filter.length;
                final int shift = size / 2;
                for (int x = shift ; x < source.pixels.length - shift ; x++) {
                    for (int y = shift ; y < source.pixels[0].length - shift ; y++) {
                        double sum = 0;
                        int k = 0;
                        for (int i = 0; i < size ; i++) {
                            for (int j = 0; j < size ; j++) {
                                k += filter[i][j];
                                sum += filter[i][j] * source.pixels[x + i - shift][y + j - shift];
                            }
                        }
                        final double p = sum / k;
                        result.pixels[x][y] = p > 1.0 ? 1.0 : (p < 0.0 ? 0.0 : p);
                    }
                }
                return result;
            }
        };
    }

    /**
     * Returns median filter instance.
     * @param size
     * @return
     */
    public static GrayscaleFilter getMedianFilter(int _filterSize) {
        return new GrayscaleFilter() {
            private final int filterSize = _filterSize;
            @Override
            public GrayscaleImage apply(GrayscaleImage source) {
                if (filterSize % 2 != 1 || filterSize < 0) {
                    throw new RuntimeException("Incorrect filter size!");
                }
                final GrayscaleImage result = new GrayscaleImage(source.getWidth(), source.getHeight());
                final int size = filterSize;
                final int shift = size / 2;
                for (int x = shift ; x < source.pixels.length - shift ; x++) {
                    for (int y = shift ; y < source.pixels[0].length - shift ; y++) {
                        final double[] sample = new double[filterSize * filterSize];
                        int k = 0;
                        for (int i = 0; i < size ; i++) {
                            for (int j = 0; j < size ; j++) {
                                sample[k++] = source.pixels[x + i - shift][y + j - shift];
                            }
                        }
                        Arrays.sort(sample);
                        result.pixels[x][y] = sample[sample.length / 2];
                    }
                }
                return result;
            }
        };
    }

    /**
     * Create a custom gradient operator filter.
     * @param _filterX
     * @param _filterY
     * @return
     */
    public static GrayscaleFilter getGradientOperatorFilter(short[][] _filterX, short[][] _filterY) {
        return new GrayscaleFilter() {
            private final short[][] filterX = _filterX;
            private final short[][] filterY = _filterY;
            @Override
            public GrayscaleImage apply(GrayscaleImage source) {
                // Filter should be odd square array.
                if (filterX.length % 2 != 1 || filterX.length != filterX[0].length
                        || filterY.length % 2 != 1 || filterY.length != filterY[0].length
                        || filterX.length != filterY.length) {
                    throw new RuntimeException("Only square odd filters are allowed.");
                }
                final GrayscaleImage result = new GrayscaleImage(source.getWidth(), source.getHeight());
                final int size = filterX.length;
                final int shift = size / 2;
                for (int x = shift ; x < source.pixels.length - shift ; x++) {
                    for (int y = shift ; y < source.pixels[0].length - shift ; y++) {
                        double gx = 0.0, gy = 0.0;
                        for (int i = 0; i < size ; i++) {
                            for (int j = 0; j < size ; j++) {
                                gx += filterX[i][j] * source.pixels[x + i - shift][y + j - shift];
                                gy += filterY[i][j] * source.pixels[x + i - shift][y + j - shift];
                            }
                        }
                        final double p = Math.sqrt(gx * gx + gy * gy);
                        result.pixels[x][y] = p > 1.0 ? 1.0 : (p < 0.0 ? 0.0 : p);
                    }
                }
                return result;
            }
        };
    }

    public static GrayscaleFilter getPrevittEdgeDetector() {
        return getGradientOperatorFilter(PREWITT_GX, PREWITT_GY);
    }

    public static GrayscaleFilter getSobelEdgeDetector() {
        return getGradientOperatorFilter(SOBEL_GX, SOBEL_GY);
    }

    /**
     * Return Normalized Cross Corellation Coefficient filter using given template.
     * @return
     */
    public static GrayscaleFilter getNCCFilter(GrayscaleImage _template) {
        return new GrayscaleFilter() {
            private final GrayscaleImage template = _template;
            @Override
            public GrayscaleImage apply(GrayscaleImage source) {
                if (template.getWidth() > source.getWidth() || template.getHeight() > source.getHeight()) {
                    throw new RuntimeException("Template can't be bigger than scene!");
                }
                final GrayscaleImage result = new GrayscaleImage(source.getWidth(), source.getHeight());
                final double sceneMean = source.getMean();
                final double templateMean = template.getMean();
                for (int a = 0; a < source.getWidth() - template.getWidth() ; a++) {
                    for (int b = 0; b < source.getHeight() - template.getHeight() ; b++) {
                        double upperSum = 0.0;
                        double lowerSumScene = 0.0;
                        double lowerSumTempl = 0.0;
                        for (int x = 0; x < template.getWidth(); x++) {
                            for (int y = 0; y < template.getHeight(); y++) {
                                upperSum += (source.pixels[x + a][y + b] - sceneMean) *
                                        (template.pixels[x][y] - templateMean);
                                lowerSumScene += Math.pow(source.pixels[x + a][y + b] - sceneMean, 2);
                                lowerSumTempl += Math.pow(template.pixels[x][y] - templateMean, 2);
                            }
                        }
                        final double p = upperSum / Math.sqrt(lowerSumScene * lowerSumTempl);
                        result.pixels[a][b] = p;
                    }
                }
                return result;
            }
        };
    }

    public static GrayscaleFilter getFilterChain(GrayscaleFilter... _filters) {
        return new GrayscaleFilter() {
            private final GrayscaleFilter[] filters = _filters;
            @Override
            public GrayscaleImage apply(GrayscaleImage source) {
                GrayscaleImage result = source;
                for (final GrayscaleFilter f : filters) {
                    result = f.apply(result);
                }
                return result;
            }
        };
    }
}
