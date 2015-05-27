package lv.rtu.dadi.facedetect.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.ImageScanVisualizer;
import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.SmartScanVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.OpenCV3DetectionCascade;

import org.apache.commons.imaging.ImageReadException;

/**
 * Compare different VJ implementations visually.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class VJComparison {

    private final static String CASCADE =
            "data\\cascades\\haarcascade_frontalface_alt.xml";

    private final static String SCENE_FILE =
//            "data\\MIT-CMU\\test-low\\trek-trio.gif";
//    "data\\miscpics\\brazil-processed.gif";
//            "data\\MIT-CMU\\test-low\\Brazil.gif";
            "data\\miscpics\\1.jpg";

    public VJComparison() {
        // TODO Auto-generated constructor stub
    }


    public static void main(String args[]) throws ImageReadException, IOException, XMLStreamException {
        final GrayscaleImage scene =
                new GrayscaleImage(ImageUtils.readImage(SCENE_FILE));

        final FaceDetector[] detectors = new FaceDetector[] {
                new SimpleVJFaceDetector(new OpenCV3DetectionCascade(new FileInputStream(CASCADE))),
                new SmartScanVJFaceDetector(new OpenCV3DetectionCascade(new FileInputStream(CASCADE))),
            };
        final List<Thread> threads = new LinkedList<Thread>();

        int idx = 0;
        for (final FaceDetector fd : detectors) {
            final ImageScanVisualizer visual = new ImageScanVisualizer(
                    String.format("Implementation %d", ++idx), scene, SCENE_FILE, false);
            fd.setVisual(visual);
            threads.add(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fd.detectFaces(scene);
                    }
                }));
        }

        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
        }
        for (final Thread t : threads) {
            t.start();
        }
    }
}
