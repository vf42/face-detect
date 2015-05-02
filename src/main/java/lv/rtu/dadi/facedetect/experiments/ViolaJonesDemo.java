package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;
import java.util.List;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.violajones.ViolaJonesFaceDetector;

import org.apache.commons.imaging.ImageReadException;

public class ViolaJonesDemo {

    public ViolaJonesDemo() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ImageReadException, IOException {
        final long startTime = System.nanoTime();

        final GrayscaleImage scene =
                new GrayscaleImage(ImageUtils.readImage("data\\MIT-CMU\\test-low\\trek-trio.gif"));
        final FaceDetector detector = new ViolaJonesFaceDetector();
        final List<FaceLocation> faces = detector.detectFaces(scene);
        new ImagePreviewWindow("V-J Result", scene, faces);

        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println(String.format("Took %.3f s", totalTime));
    }

}
