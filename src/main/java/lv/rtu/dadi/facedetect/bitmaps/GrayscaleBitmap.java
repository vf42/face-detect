package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Class for storage and basic operations on Grayscale bitmap.
 * @author fedorovvadim
 *
 */
public class GrayscaleBitmap implements Bitmap {

    public final short[][] pixels;

    public GrayscaleBitmap(BufferedImage source) {
        pixels = new short[source.getWidth()][source.getHeight()];
        for (int i = 0 ; i < source.getWidth() ; i++) {
            for (int j = 0; j < source.getHeight() ; j++) {
                final int sp = source.getRGB(i, j);
                final int r = ((sp & 0xFF0000) >> 16);
                final int g = ((sp & 0x00FF00) >> 8);
                final int b = ((sp & 0x0000FF));
                final int np = (int) Math.floor(0.2126 * r + 0.7152 * g + 0.0722 * b);
                pixels[i][j] = (short) (np > 255 ? 255 : np);
            }
        }
    }

    public GrayscaleBitmap(int width, int height, short fill) {
        pixels = new short[width][height];
        for (int i = 0 ; i < width ; i++) {
            for (int j = 0; j < height ; j++) {
                pixels[i][j] = fill;
            }
        }
    }

    public GrayscaleBitmap(int width, int height) {
        this(width, height, (short) 0);
    }

    @Override
    public Bitmap copy() {
        return null;
    }

    @Override
    public BufferedImage toBufferedImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getWidth() {
        return pixels.length;
    }

    @Override
    public int getHeight() {
        return pixels[0].length;
    }

    /**
     * Process the bitmap using provided linear filter.
     * @param filter
     * @return
     */
    public GrayscaleBitmap applyLinearFilter(short[][] filter) {
        // Filter should be odd square array.
        if (filter.length % 2 != 1 || filter.length != filter[0].length) {
            throw new RuntimeException("Only square odd filters are allowed.");
        }
        final GrayscaleBitmap result = new GrayscaleBitmap(this.getWidth(), this.getHeight());
        final int size = filter.length;
        final int shift = size / 2;
        for (int x = shift ; x < pixels.length - shift ; x++) {
            for (int y = shift ; y < pixels[0].length - shift ; y++) {
                long sum = 0;
                int k = 0;
                for (int i = 0; i < size ; i++) {
                    for (int j = 0; j < size ; j++) {
                        k += filter[i][j];
                        sum += filter[i][j] * pixels[x + i - shift][y + j - shift];
                    }
                }
                final long p = sum / k;
                result.pixels[x][y] = p > 255 ? 255 : (p < 0 ? 0 : (short) p);
            }
        }
        return result;
    }

    /**
     * Process the bitmap using median filter of the given size.
     * @param filterSize
     * @return
     */
    public GrayscaleBitmap applyMedianFilter(int filterSize) {
        if (filterSize % 2 != 1 || filterSize < 0) {
            throw new RuntimeException("Incorrect filter size!");
        }
        final GrayscaleBitmap result = new GrayscaleBitmap(this.getWidth(), this.getHeight());
        final int size = filterSize;
        final int shift = size / 2;
        for (int x = shift ; x < pixels.length - shift ; x++) {
            for (int y = shift ; y < pixels[0].length - shift ; y++) {
                final short[] sample = new short[filterSize * filterSize];
                int k = 0;
                for (int i = 0; i < size ; i++) {
                    for (int j = 0; j < size ; j++) {
                        sample[k++] = pixels[x + i - shift][y + j - shift];
                    }
                }
                Arrays.sort(sample);
                result.pixels[x][y] = sample[sample.length / 2];
            }
        }
        return result;
    }

    private GrayscaleBitmap applyGradientOperator(short[][] filterX, short[][] filterY) {
        // Filter should be odd square array.
        if (filterX.length % 2 != 1 || filterX.length != filterX[0].length
                || filterY.length % 2 != 1 || filterY.length != filterY[0].length
                || filterX.length != filterY.length) {
            throw new RuntimeException("Only square odd filters are allowed.");
        }
        final GrayscaleBitmap result = new GrayscaleBitmap(this.getWidth(), this.getHeight());
        final int size = filterX.length;
        final int shift = size / 2;
        for (int x = shift ; x < pixels.length - shift ; x++) {
            for (int y = shift ; y < pixels[0].length - shift ; y++) {
                long gx = 0, gy = 0;
                for (int i = 0; i < size ; i++) {
                    for (int j = 0; j < size ; j++) {
                        gx += filterX[i][j] * pixels[x + i - shift][y + j - shift];
                        gy += filterY[i][j] * pixels[x + i - shift][y + j - shift];
                    }
                }
                final long p = (long) Math.floor(Math.sqrt(gx * gx + gy * gy));
                result.pixels[x][y] = p > 255 ? 255 : (p < 0 ? 0 : (short) p);
            }
        }
        return result;
    }

    public GrayscaleBitmap applyPrevittEdgeDetection() {
        return applyGradientOperator(BitmapFilters.PREWITT_GX, BitmapFilters.PREWITT_GY);
    }

    public GrayscaleBitmap applySobelEdgeDetection() {
        return applyGradientOperator(BitmapFilters.SOBEL_GX, BitmapFilters.SOBEL_GY);
    }

    /**
     * Returns the average of all pixel values.
     * @return
     */
    public short getMean() {
        long sum = 0;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length ; j++) {
                sum += pixels[i][j];
            }
        }
        return (short) (sum / (pixels.length * pixels[0].length));
    }

}
