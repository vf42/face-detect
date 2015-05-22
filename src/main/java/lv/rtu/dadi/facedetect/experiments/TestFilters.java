package lv.rtu.dadi.facedetect.experiments;

import java.awt.image.BufferedImage;
import java.io.IOException;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
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
        final BufferedImage img = ImageUtils.readImage("data/FaceZ/ints_dalderis.jpg");

        final GrayscaleImage orig = new GrayscaleImage(img);
        new ImagePreviewWindow("Original", orig);

//        final GrayscaleImage filt1 = GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_1).apply(orig);
//        new ImagePreviewWindow("Filtered 1", filt1);
//        final GrayscaleImage filt2 = GrayscaleFilterFactory.getMedianFilter(5).apply(filt1);
//        new ImagePreviewWindow("Filtered 2", filt1);
//        final GrayscaleFilter sobel = GrayscaleFilterFactory.getSobelEdgeDetector();
//        new ImagePreviewWindow("Sobel from Filtered", sobel.apply(filt1));
//        new ImagePreviewWindow("Sobel from Orig", sobel.apply(orig));

        new ImagePreviewWindow("HE", GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization())
                .apply(orig));
        new ImagePreviewWindow("HE+Noise reduction", GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization(),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2))
                .apply(orig));
        new ImagePreviewWindow("HE+Sharpen", GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization(),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1))
                .apply(orig));
        new ImagePreviewWindow("Full Hi-Lo", GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization(),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2))
                .apply(orig));
        new ImagePreviewWindow("Full Lo-Hi", GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization(),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2))
                .apply(orig));

//        final GrayscaleFilter gamma = GrayscaleFilterFactory.getGammaCorrection(1.0, 0.4);
//        new ImagePreviewWindow("Gamma", gamma.apply(orig));
      }

}
