package lv.rtu.dadi.facedetect.filters;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;

/**
 * Interface for filtering.
 * @author fedorovvadim
 *
 */
public interface GrayscaleFilter {
    GrayscaleImage apply(GrayscaleImage source);
}
