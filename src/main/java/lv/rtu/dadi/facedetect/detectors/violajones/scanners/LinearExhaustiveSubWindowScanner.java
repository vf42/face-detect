package lv.rtu.dadi.facedetect.detectors.violajones.scanners;

import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;

/**
 * Separate class that provides subwindow scanning strategy.
 * @author fedorovvadim
 *
 */
public class LinearExhaustiveSubWindowScanner implements SubWindowScanner {

    /**
     *
     */
    private final SimpleVJFaceDetector violaJonesFaceDetector;

    final GrayscaleImage scene;

    final int maxWindowSize;
    final int minWindowSize;

    double sizeStep = 1.0;    // Defines how fast the window will grow.
    int windowSize;

    int shiftStep;          // Defines how fast the window will be moved over the scene.

    private SubWindow currWindow = null;
    private SubWindow nextWindow = null;

    public LinearExhaustiveSubWindowScanner(SimpleVJFaceDetector violaJonesFaceDetector, GrayscaleImage scene) {
        this.violaJonesFaceDetector = violaJonesFaceDetector;
        this.scene = scene;
        this.maxWindowSize = scene.getWidth() < scene.getHeight() ? scene.getWidth() : scene.getHeight();
        this.minWindowSize = this.violaJonesFaceDetector.getCascade().getWidth() * 2;
        this.sizeStep = 2;
        this.shiftStep = 2;

        if (Settings.DEBUG) {
            System.out.println("Min window size: " + minWindowSize);
            System.out.println("Max window size: " + maxWindowSize);
        }

        // Setup initial window.
        this.windowSize = minWindowSize;
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
                // Update size step if needed.
                if (windowSize <= 32) {
                    sizeStep += 0.5;
                } else {
                    sizeStep += 1;
                }
                windowSize += sizeStep;
                if (windowSize > maxWindowSize) {
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