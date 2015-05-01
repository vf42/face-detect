package lv.rtu.dadi.facedetect.experiments;

import java.awt.image.BufferedImage;
import java.io.IOException;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

import org.apache.commons.imaging.ImageReadException;

/**
 * Test that various filters are working.
 * @author fedorovvadim
 *
 */
public class TestFilters {

    public TestFilters() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ImageReadException, IOException {
        final BufferedImage img = ImageUtils.readImage("data/Parrot1.bmp");

        final GrayscaleImage orig = new GrayscaleImage(img);
        new ImagePreviewWindow("Original", orig);

        final GrayscaleImage filt1 = GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_1).apply(orig);
        new ImagePreviewWindow("Filtered 1", filt1);
        final GrayscaleImage filt2 = GrayscaleFilterFactory.getMedianFilter(5).apply(filt1);
        new ImagePreviewWindow("Filtered 2", filt1);
        final GrayscaleFilter sobel = GrayscaleFilterFactory.getSobelEdgeDetector();
        new ImagePreviewWindow("Sobel from Filtered", sobel.apply(filt1));
        new ImagePreviewWindow("Sobel from Orig", sobel.apply(orig));
      }

}
