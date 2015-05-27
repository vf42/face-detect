package lv.rtu.dadi.facedetect.external;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import detection.Detector;

/**
 * An adapter to jviolajones face detection library.
 * @author fedorovvadim
 *
 */
public class JViolaJonesDetector implements FaceDetector {

    private final static String CASCADE_FILE = "lib/jviolajones_cascade.xml";

    private final Detector detector;

    public JViolaJonesDetector() {
        this.detector = Detector.create(CASCADE_FILE);
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        if (!(scene instanceof FakeGrayscaleImage)) {
            throw new RuntimeException("FakeGrayscaleImage should be used here!");
        }
        final String filename = ((FakeGrayscaleImage) scene).getFilename();
        if (filename == null) {
            throw new RuntimeException("No file provided!");
        }
        final List<Rectangle> alienResult = detector.getFaces(filename, 1.2f, 1.1f, 0.05f, 2, true);
        final List<FaceLocation> ownResult = new ArrayList<>();
        for (final Rectangle r : alienResult) {
            ownResult.add(new FaceLocation(r.x, r.y, r.width, r.height));
        }
        return ownResult;
    }

}
