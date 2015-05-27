package lv.rtu.dadi.facedetect.external;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Wrapper for OpenCV face detection.
 * @author fedorovvadim
 *
 */
public class OpenCVDetector implements FaceDetector {

    private final static String CASCADE_FILE = "cascades/haarcascade_frontalface_alt.xml";

    private final CascadeClassifier faceDetector;

    public OpenCVDetector() {
        System.loadLibrary("lib/" + Core.NATIVE_LIBRARY_NAME);
//        final String cascadePath = ClassLoader.getSystemClassLoader().getResource(CASCADE_FILE).getPath();
        final String cascadePath = "lib/haarcascade_frontalface_alt.xml";
        faceDetector = new CascadeClassifier(cascadePath);
        if (faceDetector.empty()) {
            throw new RuntimeException("EMPTY!");
        }
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
        // Perform the detection.
        final Mat image = Imgcodecs.imread(filename);
        final MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        return Arrays.asList(Stream.of(faceDetections.toArray())
                .map(r -> new FaceLocation(r.x, r.y, r.width, r.height))
                .toArray(n -> new FaceLocation[n]));
    }

}
