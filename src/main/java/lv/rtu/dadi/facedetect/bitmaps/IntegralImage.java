package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;

/**
 * Integral image implementation for Haar-like feature computation.
 * @author fedorovvadim
 *
 */
public class IntegralImage implements Image {

    public final double[][] values;

    public IntegralImage(GrayscaleImage source) {
        values = new double[source.getWidth()][source.getHeight()];
        for (int xi = 0; xi < source.getWidth(); xi++) {
            for (int yi = 0; yi < source.getHeight(); yi++) {
                double sum = source.pixels[xi][yi];
                if (xi > 0) sum += values[xi - 1][yi];
                if (yi > 0) sum += values[xi][yi - 1];
                if (xi > 0 && yi > 0) sum -= values[xi - 1][yi - 1];
                values[xi][yi] = sum;
            }
        }
    }

    /**
     * Return the sum value for the pixels in specified rectangle.
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public double getRectValue(int x, int y, int w, int h) {
        return values[x + w][y + h] + values[x][y] - values[x + w][y] - values[x][y + h];
    }

    @Override
    public Image copy() {
        throw new RuntimeException("Not applicable.");
    }

    @Override
    public BufferedImage toBufferedImage() {
        throw new RuntimeException("Not applicable.");
    }

    @Override
    public int getWidth() {
        return values.length;
    }

    @Override
    public int getHeight() {
        return values[0].length;
    }

}
