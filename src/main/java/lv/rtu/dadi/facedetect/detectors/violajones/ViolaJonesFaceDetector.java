package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.haar.IntegralImage;

/**
 * Implementation of Viola-Jones face detection, using the prepared cascade.
 * @author fedorovvadim
 *
 */
public class ViolaJonesFaceDetector implements FaceDetector {

    private final DetectionCascade cascade;

    public ViolaJonesFaceDetector() {
        // Init detection cascade.
        this.cascade = new StupidDetectionCascade();
    }

    public ViolaJonesFaceDetector(DetectionCascade cascade) {
        this.cascade = cascade;
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        final List<FaceLocation> rawResult = new LinkedList<>();
        final IntegralImage integralImg = new IntegralImage(scene);
        final int maxWindowSize = scene.getWidth() < scene.getHeight() ? scene.getWidth() : scene.getHeight();
        final int minWindowSize = cascade.getWidth();
        int sizeStep = 2;
        if (Settings.DEBUG) {
            System.out.println("Min window size: " + minWindowSize);
            System.out.println("Max window size: " + maxWindowSize);
        }
        // Perform the scan.
        for (int windowSize = minWindowSize; windowSize <= maxWindowSize; windowSize += sizeStep) {
            if (windowSize >= scene.getWidth()) {
                break;
            }
            int shiftStep = windowSize / 16;
            if (shiftStep < 2) shiftStep = 2;
            final SubWindow sw = new SubWindow(0, 0, windowSize, windowSize);
            for ( ; sw.x < integralImg.getWidth() - sw.w ; sw.x += shiftStep) {
                for (sw.y = 0 ; sw.y < integralImg.getHeight() - sw.h ; sw.y += shiftStep) {
                    if (cascade.testWindow(integralImg, sw)) {
                        rawResult.add(new FaceLocation(sw));
                    }
                }
            }
            sizeStep += 2; // The bigger window gets, the faster it grows.
        }
        return rawResult;
    }


}
