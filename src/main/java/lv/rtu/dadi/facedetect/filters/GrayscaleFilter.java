package lv.rtu.dadi.facedetect.filters;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleBitmap;

/**
 * Interface for filtering.
 * @author fedorovvadim
 *
 */
public interface GrayscaleFilter {
    GrayscaleBitmap apply(GrayscaleBitmap source);
}
