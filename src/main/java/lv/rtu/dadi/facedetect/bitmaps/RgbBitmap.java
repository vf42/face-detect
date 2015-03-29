package lv.rtu.dadi.facedetect.bitmaps;

import java.awt.image.BufferedImage;

/**
 * Class for storage and basic operations on RGB bitmap.
 *
 * @author fedorovvadim
 *
 */
public class RgbBitmap implements Bitmap {

    public class RgbPixel {
        public final int r, g, b, i;
        public RgbPixel(int r, int g, int b, int i) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.i = i;
        }
        public RgbPixel(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.i = (int) Math.floor(0.2126 * r + 0.7152 * g + 0.0722 * b);
        }
    }

    public final RgbPixel[][] pixels;

    public RgbBitmap(BufferedImage source) {
        pixels = new RgbPixel[source.getWidth()][source.getHeight()];
        for (int i = 0 ; i < source.getWidth() ; i++) {
            for (int j = 0; j < source.getHeight() ; j++) {
                final int p = source.getRGB(i, j);
                pixels[i][j] = new RgbPixel(
                        ((p & 0xFF0000) >> 16),
                        ((p & 0x00FF00) >> 8),
                        (p & 0x0000FF)
                        );
            }
        }
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

    @Override
    public Bitmap copy() {
        return null;
    }
}
