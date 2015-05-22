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
import lv.rtu.dadi.facedetect.detectors.mergers.FaceCenterMerger;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.OpenCV3DetectionCascade;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

import org.apache.commons.imaging.ImageReadException;

public class VJWithPreprocessing {

    private final static String cascadeFile =
          "C:\\Soft\\opencv30rc1\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml";

  private final static String sceneFile =
//          "data\\MIT-CMU\\test-low\\trek-trio.gif";
//          "data\\MIT-CMU\\test-low\\USA.gif";
//          "data\\MIT-CMU\\rotated\\rot6.gif";
//          "data\\strongrace\\1.JPG";
          "data\\miscpics\\4.JPG";
//          "data\\MIT-CMU\\test\\original2.gif";
//          "data\\MIT-CMU\\newtest\\newsradio.gif";
//          "data\\MIT-CMU\\newtest\\addams-family.gif";
//          "data\\MIT-CMU\\newtest\\class57.gif";
//          "data\\MIT-CMU\\newtest\\er.gif";
//          "data\\MIT-CMU\\newtest\\nens.gif";

  public static void main(String[] args) throws ImageReadException, IOException, XMLStreamException {
      final long startTime = System.nanoTime();

      // Using the OpenCV3 cascade.
      final FaceDetector detector = new SimpleVJFaceDetector(
              new OpenCV3DetectionCascade(
                      new FileInputStream(cascadeFile)),
              new FaceCenterMerger(3, false));

      final GrayscaleImage scene =
              new GrayscaleImage(ImageUtils.readImage(sceneFile));

      // First preprocessor with edge detection.
      final GrayscaleFilter preprocessor =
              GrayscaleFilterFactory.getFilterChain(
//                      GrayscaleFilterFactory.getGammaCorrection(1.0, 1.4),
                      GrayscaleFilterFactory.getHistogramEquialization(),
                      GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                      GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2));

      final GrayscaleImage processedScene = preprocessor.apply(scene);

      final List<FaceLocation> faces = detector.detectFaces(scene);
      new ImagePreviewWindow("Raw", scene, faces);

      final List<FaceLocation> faces2 = detector.detectFaces(processedScene);
      new ImagePreviewWindow("Processed", processedScene, faces2);

      final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;

      System.out.println(String.format("Processing time: %.3fs", totalTime));
  }

}
