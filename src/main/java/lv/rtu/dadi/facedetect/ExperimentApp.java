package lv.rtu.dadi.facedetect;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleBitmap;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.SimpleCorrelationDetector;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;
import magick.MagickException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BufferedImageFactory;


/**
 * Basic skeleton for experimenting with various algorithms.
 * @author fedorovvadim
 *
 */
public class ExperimentApp {

    public static class ManagedImageBufferedImageFactory implements BufferedImageFactory {

        @Override
        public BufferedImage getColorBufferedImage(final int width, final int height, final boolean hasAlpha) {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        }

        @Override
        public BufferedImage getGrayscaleBufferedImage(final int width, final int height, final boolean hasAlpha) {
            return getColorBufferedImage(width, height, hasAlpha);
        }
    }

    private BufferedImage readImage(String fname) throws ImageReadException, IOException {
        final Map<String, Object> params = new HashMap<String, Object>();

        // set optional parameters if you like
        params.put(ImagingConstants.BUFFERED_IMAGE_FACTORY, new ManagedImageBufferedImageFactory());

        // params.put(ImagingConstants.PARAM_KEY_VERBOSE, Boolean.TRUE);

        // read image
        final BufferedImage image = Imaging.getBufferedImage(new File(fname), params);

        return image;
    }

    private void testFilters() throws ImageReadException, IOException {
      final BufferedImage img = readImage("data/Parrot1.bmp");

      final GrayscaleBitmap orig = new GrayscaleBitmap(img);
      new ImagePreviewWindow("Original", orig);

      final GrayscaleBitmap filt1 = GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_1).apply(orig);
      new ImagePreviewWindow("Filtered 1", filt1);
      final GrayscaleBitmap filt2 = GrayscaleFilterFactory.getMedianFilter(5).apply(filt1);
      new ImagePreviewWindow("Filtered 2", filt1);
      final GrayscaleFilter sobel = GrayscaleFilterFactory.getSobelEdgeDetector();
      new ImagePreviewWindow("Sobel from Filtered", sobel.apply(filt1));
      new ImagePreviewWindow("Sobel from Orig", sobel.apply(orig));
    }

    private void simpleCorellation() throws ImageReadException, IOException {
//        final GrayscaleBitmap template = new GrayscaleBitmap(readImage("data/smiley/sample4.jpg"));
//        final GrayscaleBitmap scene = new GrayscaleBitmap(readImage("data/smiley/scene1.bmp"));

        final GrayscaleBitmap template = new GrayscaleBitmap(readImage("data/toys1/face1.bmp"));
        final GrayscaleBitmap scene = new GrayscaleBitmap(readImage("data/toys1/scene5.bmp"));

        final GrayscaleFilter preprocessor = GrayscaleFilterFactory.getFilterChain(
//                GrayscaleFilterFactory.getMedianFilter(3),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                GrayscaleFilterFactory.getSobelEdgeDetector()
//                GrayscaleFilterFactory.getPrevittEdgeDetector()
                );

        final GrayscaleBitmap procTempl = preprocessor.apply(template);
        final GrayscaleBitmap procScene = preprocessor.apply(scene);

        final FaceDetector detector = new SimpleCorrelationDetector(procTempl, 0.3);
        final List<FaceLocation> faces = detector.detectFaces(procScene);
        System.out.println(faces.size() + " match candidates detected");
        new ImagePreviewWindow("Result", scene, faces);
    }

    public static void main(String[] args) throws MagickException, ImageReadException, IOException {
        final ExperimentApp expApp = new ExperimentApp();
//        expApp.testFilters();
        expApp.simpleCorellation();
    }
}
