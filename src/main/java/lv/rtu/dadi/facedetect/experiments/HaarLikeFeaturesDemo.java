package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;
import lv.rtu.dadi.facedetect.haar.FourRectHLF;
import lv.rtu.dadi.facedetect.haar.HaarLikeFeature;
import lv.rtu.dadi.facedetect.haar.ThreeRectHorizontalHLF;
import lv.rtu.dadi.facedetect.haar.TwoRectHorizontalHLF;
import lv.rtu.dadi.facedetect.haar.TwoRectVerticalHLF;

import org.apache.commons.imaging.ImageReadException;

public class HaarLikeFeaturesDemo {

    private final static int FEATURE_SIZE = 8;

    public HaarLikeFeaturesDemo() {
        // TODO Auto-generated constructor stub
    }

    public static void haar1(IntegralImage ii) {
        final HaarLikeFeature hlf = new TwoRectHorizontalHLF(FEATURE_SIZE, FEATURE_SIZE);
        final double[][] haarResult = hlf.regionScan(ii, 0, 0, ii.getWidth(), ii.getHeight());
        final GrayscaleImage output =
                GrayscaleFilterFactory.getValueNormalizerFilter().apply(new GrayscaleImage(haarResult));
        new ImagePreviewWindow("Two-rect horizontal", output);
    }

    public static void haar2(IntegralImage ii) {
        final HaarLikeFeature hlf = new TwoRectVerticalHLF(16, 6);
        final double[][] haarResult = hlf.regionScan(ii, 0, 0, ii.getWidth(), ii.getHeight());
        final GrayscaleImage output =
                GrayscaleFilterFactory.getValueNormalizerFilter().apply(new GrayscaleImage(haarResult));
        new ImagePreviewWindow("Two-rect vertical", output);
    }

    public static void haar3(IntegralImage ii) {
        final HaarLikeFeature hlf = new ThreeRectHorizontalHLF(FEATURE_SIZE, FEATURE_SIZE);
        final double[][] haarResult = hlf.regionScan(ii, 0, 0, ii.getWidth(), ii.getHeight());
        final GrayscaleImage output =
                GrayscaleFilterFactory.getValueNormalizerFilter().apply(new GrayscaleImage(haarResult));
        new ImagePreviewWindow("Three-rect", output);
    }

    public static void haar4(IntegralImage ii) {
        final HaarLikeFeature hlf = new FourRectHLF(FEATURE_SIZE, FEATURE_SIZE);
        final double[][] haarResult = hlf.regionScan(ii, 0, 0, ii.getWidth(), ii.getHeight());
        final GrayscaleImage output =
                GrayscaleFilterFactory.getValueNormalizerFilter().apply(new GrayscaleImage(haarResult));
        new ImagePreviewWindow("Four-rect", output);
    }

    /**
     * Demonstrate shaded region detection using feature #2.
     * @param scene
     * @param ii
     */
    public static void twoRectOverlay(GrayscaleImage scene, IntegralImage ii) {
        final HaarLikeFeature hlf = new TwoRectVerticalHLF(FEATURE_SIZE * 2, FEATURE_SIZE);
        final double[][] haarResult = hlf.regionScan(ii, 0, 0, ii.getWidth(), ii.getHeight());
        final GrayscaleImage output =
                GrayscaleFilterFactory.getValueNormalizerFilter().apply(new GrayscaleImage(haarResult));
        final FaceDetector detector = new FaceDetector() {
            @Override
            public List<FaceLocation> detectFaces(GrayscaleImage scene) {
                final List<FaceLocation> faces = new LinkedList<>();
                for (int i = 0; i < scene.getWidth() - hlf.width; i++) {
                    for (int j = 0; j < scene.getHeight() - hlf.height; j++) {
                        if (output.pixels[i][j] > 0.6) {
                            faces.add(new FaceLocation(i, j, hlf.width, hlf.height));
                        }
                    }
                }
                return faces;
            }
        };
        final List<FaceLocation> faces = detector.detectFaces(scene);
        new ImagePreviewWindow("Feature map", output);
        new ImagePreviewWindow("Detection result", scene, faces);
    }

    public static void main(String[] args) throws ImageReadException, IOException {
        final GrayscaleImage scene = new GrayscaleImage(ImageUtils.readImage("data\\MIT-CMU\\test-low\\trek-trio.gif"));
        new ImagePreviewWindow("Original", scene);
        final IntegralImage ii = new IntegralImage(scene);
        haar1(ii);
        haar2(ii);
        haar3(ii);
        haar4(ii);
//        twoRectOverlay(scene, ii);
    }

}
