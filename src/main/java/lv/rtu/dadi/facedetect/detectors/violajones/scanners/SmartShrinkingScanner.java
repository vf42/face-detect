package lv.rtu.dadi.facedetect.detectors.violajones.scanners;

import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.ViolaJonesContext;

/**
 * A scanner that starts with biggest window sizes and doesn't go
 * into already detected face areas with smaller windows.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class SmartShrinkingScanner implements SubWindowScanner {

    final SimpleVJFaceDetector detector;

    final GrayscaleImage scene;

    final int maxWindowSize;
    final int minWindowSize;

    double sizeStep = 0.9;    // Defines how fast the window will shrink.
    int windowSize;

    int shiftStep;          // Defines how fast the window will be moved over the scene.

    private SubWindow currWindow = null;
    private SubWindow nextWindow = null;

    public SmartShrinkingScanner(SimpleVJFaceDetector violaJonesFaceDetector, ViolaJonesContext context) {
        this.detector = violaJonesFaceDetector;
        this.scene = context.scene;
        this.maxWindowSize = scene.getWidth() < scene.getHeight() ? scene.getWidth() : scene.getHeight();
        this.minWindowSize = this.detector.getCascade().getWidth() * 2;
        this.shiftStep = 2;

        if (Settings.DEBUG) {
            System.out.println("Min window size: " + minWindowSize);
            System.out.println("Max window size: " + maxWindowSize);
        }

        // Setup initial window.
        this.windowSize = maxWindowSize;
        this.nextWindow = new SubWindow(0, 0, windowSize, windowSize);
    }

    @Override
    public boolean hasNext() {
        return nextWindow != null;
    }

    @Override
    public SubWindow next() {
        if (nextWindow == null) {
            return null;
        }
        currWindow = nextWindow.clone();
        // Move to next position.
        nextWindow.y += shiftStep;
        if (nextWindow.y >= scene.getHeight() - nextWindow.h) {
            // Reached max y.
            nextWindow.y = 0;
            nextWindow.x += shiftStep;
            if (nextWindow.x >= scene.getWidth() - nextWindow.w) {
                // Reached max x.
                nextWindow.x = 0;
                windowSize *= sizeStep;
                if (windowSize < minWindowSize) {
                    // Reached max size, no next.
                    nextWindow = null;
                } else {
                    nextWindow.w = windowSize;
                    nextWindow.h = windowSize;
                }
            }
        }
        return currWindow;
    }

}
