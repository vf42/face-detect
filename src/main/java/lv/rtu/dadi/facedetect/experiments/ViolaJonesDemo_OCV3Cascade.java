package lv.rtu.dadi.facedetect.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.FaceMerger;
import lv.rtu.dadi.facedetect.detectors.violajones.OpenCV3DetectionCascade;
import lv.rtu.dadi.facedetect.detectors.violajones.ViolaJonesFaceDetector;

import org.apache.commons.imaging.ImageReadException;

public class ViolaJonesDemo_OCV3Cascade {

    private final static String cascadeFile =
//            "data\\cascades\\my_stupid_cascade.xml";
//            "C:\\Soft\\opencv30rc1\\build\\etc\\haarcascades\\haarcascade_frontalface_default.xml";
            "C:\\Soft\\opencv30rc1\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml";
//            "C:\\Soft\\opencv30rc1\\build\\etc\\haarcascades\\haarcascade_eye.xml";
//            "C:\\Soft\\opencv30rc1\\build\\etc\\haarcascades\\haarcascade_frontalface_alt_tree.xml";

    private final static String sceneFile =
            "data\\MIT-CMU\\test-low\\trek-trio.gif";
//            "data\\strongrace\\1.JPG";
//            "data\\MIT-CMU\\test\\original2.gif";
//            "data\\MIT-CMU\\newtest\\newsradio.gif";
//            "data\\MIT-CMU\\newtest\\addams-family.gif";
//            "data\\MIT-CMU\\newtest\\class57.gif";
//            "data\\MIT-CMU\\newtest\\er.gif";

    public ViolaJonesDemo_OCV3Cascade() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ImageReadException, IOException, XMLStreamException {
        final long startTime = System.nanoTime();

        // Using the OpenCV3 cascade.
        final FaceDetector detector = new ViolaJonesFaceDetector(
                new OpenCV3DetectionCascade(
                        new FileInputStream(cascadeFile)));

        final GrayscaleImage scene =
                new GrayscaleImage(ImageUtils.readImage(sceneFile));

        final List<FaceLocation> faces = detector.detectFaces(scene);
//        new ImagePreviewWindow("V-J Result", scene, faces);

        final List<FaceLocation> mergedFaces2 = FaceMerger.faceCenterMerge(faces);
        new ImagePreviewWindow("Merged Result", scene, mergedFaces2);

        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println(String.format("Processing time: %.3fs", totalTime));
    }

}
