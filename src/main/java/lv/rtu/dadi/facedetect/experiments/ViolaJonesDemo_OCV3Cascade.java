package lv.rtu.dadi.facedetect.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImageScanVisualizer;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.mergers.FaceCenterMerger;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.OpenCV3DetectionCascade;

import org.apache.commons.imaging.ImageReadException;

public class ViolaJonesDemo_OCV3Cascade {

    private final static String cascadeFile =
            "data\\cascades\\haarcascade_frontalface_alt.xml";

    private final static String sceneFile =
//            "data\\MIT-CMU\\test-low\\Brazil.gif";
//            "data\\MIT-CMU\\test-low\\trekcolr.gif";
//            "data\\MIT-CMU\\test-low\\trek-trio.gif";
//            "data\\strongrace\\1.JPG";
            "data\\miscpics\\1.jpg";
//            "data\\MIT-CMU\\test\\original2.gif";
//            "data\\MIT-CMU\\newtest\\newsradio.gif";
//            "data\\MIT-CMU\\newtest\\addams-family.gif";
//            "data\\MIT-CMU\\newtest\\class57.gif";
//            "data\\MIT-CMU\\newtest\\er.gif";

    public ViolaJonesDemo_OCV3Cascade() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ImageReadException, IOException, XMLStreamException {
        // Using the OpenCV3 cascade.
        final FaceDetector detector = new SimpleVJFaceDetector(
                new OpenCV3DetectionCascade(
                        new FileInputStream(cascadeFile)),
                new FaceCenterMerger(3));

        final GrayscaleImage scene =
                new GrayscaleImage(ImageUtils.readImage(sceneFile));

        final ImageScanVisualizer visual = new ImageScanVisualizer("Process", scene, sceneFile, false);
        detector.setVisual(visual);

        final long startTime = System.nanoTime();

        final List<FaceLocation> faces = detector.detectFaces(scene);

        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println(String.format("Processing time: %.3fs", totalTime));

        new ImagePreviewWindow("Result", scene, faces);
//        new ImagePreviewWindow("V-J Result", scene, faces);
    }

}
