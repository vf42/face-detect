package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.OptimalVJFaceDetector;
import lv.rtu.dadi.facedetect.external.FakeGrayscaleImage;
import lv.rtu.dadi.facedetect.external.JViolaJonesDetector;
import lv.rtu.dadi.facedetect.external.OpenCVDetector;

import org.apache.commons.imaging.ImageReadException;

/**
 * Comparing my detector, jviolajones and OpenCV.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class CompareDifferentImplementations {

    private final static String SCENE_FILE = "data/strongrace/4.jpg";

    public static void main(String[] args) throws ImageReadException, IOException, XMLStreamException {
        final GrayscaleImage scene =
                new GrayscaleImage(ImageUtils.readImage(SCENE_FILE));
        final GrayscaleImage fakeScene =
                new FakeGrayscaleImage(SCENE_FILE);
        final GrayscaleImage fakeSceneOcv =
                new FakeGrayscaleImage(SCENE_FILE + ".jpg");

        final FaceDetector ownDetector = new OptimalVJFaceDetector();
        new ImagePreviewWindow("Own", scene, ownDetector.detectFaces(scene));

        final FaceDetector ocvDetector = new OpenCVDetector();
        new ImagePreviewWindow("OpenCV", scene, ocvDetector.detectFaces(fakeScene));

        final FaceDetector jvjDetector = new JViolaJonesDetector();
        new ImagePreviewWindow("jviolajones", scene, jvjDetector.detectFaces(fakeScene));
    }

}
