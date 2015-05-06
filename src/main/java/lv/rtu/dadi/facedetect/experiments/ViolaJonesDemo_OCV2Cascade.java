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
import lv.rtu.dadi.facedetect.detectors.violajones.OpenCV2DetectionCascade;
import lv.rtu.dadi.facedetect.detectors.violajones.ViolaJonesFaceDetector;

import org.apache.commons.imaging.ImageReadException;

public class ViolaJonesDemo_OCV2Cascade {

    private final static String cascadeFile =
          "data\\cascades\\jviolajones_cascade.xml";

  private final static String sceneFile =
//          "data\\primitives\\white_point.png";
          "data\\MIT-CMU\\test-low\\trek-trio.gif";
//          "data\\strongrace\\1.JPG";
//          "data\\MIT-CMU\\test\\original2.gif";
//          "data\\MIT-CMU\\newtest\\newsradio.gif";

  public static void main(String[] args) throws ImageReadException, IOException, XMLStreamException {
      final long startTime = System.nanoTime();

      // Using the OpenCV2 cascade.
      final FaceDetector detector = new ViolaJonesFaceDetector(
              new OpenCV2DetectionCascade(
                      new FileInputStream(cascadeFile)));

      final GrayscaleImage scene =
              new GrayscaleImage(ImageUtils.readImage(sceneFile));

      final List<FaceLocation> faces = detector.detectFaces(scene);
      new ImagePreviewWindow("V-J Result", scene, faces);

      final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;

      System.out.println(String.format("Took %.3f s", totalTime));
  }

}
