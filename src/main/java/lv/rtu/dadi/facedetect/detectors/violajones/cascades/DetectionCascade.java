package lv.rtu.dadi.facedetect.detectors.violajones.cascades;

import lv.rtu.dadi.facedetect.ImageScanVisualizer;
import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.violajones.ViolaJonesStats;

/**
 * An interface for different cascade implementations.
 */
public interface DetectionCascade {
    default boolean testWindow(IntegralImage ii, SubWindow sw) {
        return testWindow(ii, sw, null);
    }
    boolean testWindow(IntegralImage ii, SubWindow sw, ViolaJonesStats stats);

    int getWidth();
    int getHeight();

    default int getFeatureCount() {
        return 0;
    }

    default void setVisual(ImageScanVisualizer visual) { }
}
