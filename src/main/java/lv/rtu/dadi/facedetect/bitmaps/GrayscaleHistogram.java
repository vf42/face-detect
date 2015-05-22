package lv.rtu.dadi.facedetect.bitmaps;

import java.util.Arrays;

/**
 * Computes and stores a histogram from Grayscale Image.
 * Pixel values are converted to int 0..255 representation.
 *
 * @author fedorovvadim
 *
 */
public class GrayscaleHistogram {

    public final int[] values = new int[256];

    public GrayscaleHistogram(GrayscaleImage img) {
        Arrays.fill(values, 0);
        for (int i = 0; i < img.pixels.length; i++) {
            for (int j = 0; j < img.pixels.length; j++) {
                final int pixelVal = (int) (img.pixels[i][j] * 255);
                ++values[pixelVal];
            }
        }
    }

}