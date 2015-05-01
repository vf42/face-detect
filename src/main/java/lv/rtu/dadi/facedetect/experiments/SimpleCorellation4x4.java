package lv.rtu.dadi.facedetect.experiments;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.SimpleCorrelationDetector;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

import org.apache.commons.imaging.ImageReadException;

public class SimpleCorellation4x4 {

    public SimpleCorellation4x4() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ImageReadException, IOException {
        final GrayscaleImage template = new GrayscaleImage(ImageUtils.readImage("data/toys1/face1.bmp"));

        for (final File f : new File("data/toys1").listFiles()) {
            if (!f.isFile() || !f.getName().contains("scene3")) {
                continue;
            }
            System.out.println(f.getAbsolutePath());
            final GrayscaleImage scene = new GrayscaleImage(ImageUtils.readImage(f.getAbsolutePath()));

            // Using 4x4 split.
            final GrayscaleFilter preprocessor = GrayscaleFilterFactory.getFilterChain(
                    GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2),
                    GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                    GrayscaleFilterFactory.getSobelEdgeDetector(),
                    GrayscaleFilterFactory.getRegionFilter(4, 0, true),
                    GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2)
                    );

            final GrayscaleImage procTempl = preprocessor.apply(template);
            final GrayscaleImage procScene = preprocessor.apply(scene);

            final FaceDetector detector = new SimpleCorrelationDetector(procTempl, 0.5);
            final List<FaceLocation> faces = detector.detectFaces(procScene);
            System.out.println(faces.size() + " match candidates detected");
            final List<FaceLocation> processedFaces =
                    faces.stream().map(fl -> new FaceLocation(fl.x * 4, fl.y * 4, fl.w * 4, fl.h * 4)).collect(Collectors.toList());
            new ImagePreviewWindow("Result", scene, processedFaces, 1);
        }
      }
}
