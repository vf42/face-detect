package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;

import lv.rtu.dadi.facedetect.haar.HaarRectangle;

/**
 * Integral image implementation for Haar-like feature computation.
 * Pixel values are converted to int 0..255 representation.
 *
 * @author fedorovvadim
 *
 */
public class IntegralImage implements Image {

    public final long[][] values;     // Simple II.
    public final long[][] sqValues;   // Square II.

    public IntegralImage(GrayscaleImage source) {
        values = new long[source.getWidth()][source.getHeight()];
        sqValues = new long[source.getWidth()][source.getHeight()];
        for (int xi = 0; xi < source.getWidth(); xi++) {
            for (int yi = 0; yi < source.getHeight(); yi++) {
                long sum = (long) (source.pixels[xi][yi] * 255);
                long sqSum = (long) (source.pixels[xi][yi] * source.pixels[xi][yi] * 255 * 255);
                if (xi > 0) {
                    sum += values[xi - 1][yi];
                    sqSum += sqValues[xi - 1][yi];
                }
                if (yi > 0) {
                    sum += values[xi][yi - 1];
                    sqSum += sqValues[xi][yi - 1];
                }
                if (xi > 0 && yi > 0) {
                    sum -= values[xi - 1][yi - 1];
                    sqSum -= sqValues[xi - 1][yi - 1];
                }
                values[xi][yi] = sum;
                sqValues[xi][yi] = sqSum;
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
    public long getRectValue(int x, int y, int w, int h) {
        return values[x + w][y + h] + values[x][y] - values[x + w][y] - values[x][y + h];
    }

    public long getRectValue(int baseX, int baseY, HaarRectangle rect) {
        try {
            return values[baseX + rect.x1][baseY + rect.y1] + values[baseX + rect.x0][baseY + rect.y0]
                    - values[baseX + rect.x1][baseY + rect.y0] - values[baseX + rect.x0][baseY + rect.y1];
        } catch (final ArrayIndexOutOfBoundsException ex) {
            return values[values.length - 1][values[0].length - 1] + values[baseX + rect.x0][baseY + rect.y0]
                    - values[values.length - 1][baseY + rect.y0] - values[baseX + rect.x0][values[0].length - 1];
        }
    }

    public long getSqRectValue(int baseX, int baseY, HaarRectangle rect) {
        try {
            return sqValues[baseX + rect.x1][baseY + rect.y1] + sqValues[baseX + rect.x0][baseY + rect.y0]
                    - sqValues[baseX + rect.x1][baseY + rect.y0] - sqValues[baseX + rect.x0][baseY + rect.y1];
        } catch (final ArrayIndexOutOfBoundsException ex) {
            return sqValues[values.length - 1][values[0].length - 1] + sqValues[baseX + rect.x0][baseY + rect.y0]
                    - sqValues[values.length - 1][baseY + rect.y0] - sqValues[baseX + rect.x0][values[0].length - 1];
        }
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
