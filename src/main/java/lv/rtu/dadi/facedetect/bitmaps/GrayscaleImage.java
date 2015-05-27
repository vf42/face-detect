package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;

/**
 * Class for storage and basic operations on Grayscale bitmap.
 * Stores pixel values as float [0.0 .. 1.0]
 *
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class GrayscaleImage implements Image {

    public final double[][] pixels;

    public GrayscaleImage(BufferedImage source) {
        pixels = new double[source.getWidth()][source.getHeight()];
        for (int i = 0 ; i < source.getWidth() ; i++) {
            for (int j = 0; j < source.getHeight() ; j++) {
                final int sp = source.getRGB(i, j);
                final int r = ((sp & 0xFF0000) >> 16);
                final int g = ((sp & 0x00FF00) >> 8);
                final int b = ((sp & 0x0000FF));
                final int np = (int) Math.floor(0.2126 * r + 0.7152 * g + 0.0722 * b);
                final int intPixel = (np > 255 ? 255 : np);
                pixels[i][j] = intPixel / 255.0;
            }
        }
    }

    public GrayscaleImage(int width, int height, double fill) {
        pixels = new double[width][height];
        for (int i = 0 ; i < width ; i++) {
            for (int j = 0; j < height ; j++) {
                pixels[i][j] = fill;
            }
        }
    }

    public GrayscaleImage(double[][] pixels) {
        this.pixels = pixels;
    }

    public GrayscaleImage(int width, int height) {
        this(width, height, (short) 0);
    }

    @Override
    public Image copy() {
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
     * Returns the average of all pixel values.
     * @return
     */
    public double getMean() {
        double sum = 0;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length ; j++) {
                sum += pixels[i][j];
            }
        }
        return sum / (pixels.length * pixels[0].length);
    }

    /**
     * Returns the maximum of all pixel values.
     * @return
     */
    public double getMax() {
        double max = pixels[0][0];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length ; j++) {
                max = max < pixels[i][j] ? pixels[i][j] : max;
            }
        }
        return max;
    }

    /**
     * Returns the minimum of all pixel values.
     * @return
     */
    public double getMin() {
        double min = pixels[0][0];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length ; j++) {
                min = min > pixels[i][j] ? pixels[i][j] : min;
            }
        }
        return min;
    }
}
