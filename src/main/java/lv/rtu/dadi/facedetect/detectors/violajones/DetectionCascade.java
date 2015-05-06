package lv.rtu.dadi.facedetect.detectors.violajones;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;

/**
 * An interface for different cascade implementations.
 */
public interface DetectionCascade {
    public boolean testWindow(IntegralImage ii, SubWindow sw);

    public int getWidth();
    public int getHeight();
}
