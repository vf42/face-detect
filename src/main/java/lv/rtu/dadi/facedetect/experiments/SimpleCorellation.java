package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;
import java.util.List;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.SimpleCorrelationDetector;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

import org.apache.commons.imaging.ImageReadException;



public class SimpleCorellation {

    public SimpleCorellation() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ImageReadException, IOException {
        final GrayscaleImage template = new GrayscaleImage(ImageUtils.readImage("data/toys1/face1.bmp"));

        final GrayscaleImage scene = new GrayscaleImage(ImageUtils.readImage("data/toys1/scene5.bmp"));

        // First preprocessor with edge detection.
        final GrayscaleFilter preprocessor =
                GrayscaleFilterFactory.getFilterChain(GrayscaleFilterFactory
                        .getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2), GrayscaleFilterFactory
                        .getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1), GrayscaleFilterFactory
                        .getSobelEdgeDetector());

        final GrayscaleImage procTempl = preprocessor.apply(template);
        final GrayscaleImage procScene = preprocessor.apply(scene);

        final FaceDetector detector = new SimpleCorrelationDetector(procTempl, 0.3);
        final List<FaceLocation> faces = detector.detectFaces(procScene);
        System.out.println(faces.size() + " match candidates detected");
        new ImagePreviewWindow("Result", scene, faces, 1);
    }

}
