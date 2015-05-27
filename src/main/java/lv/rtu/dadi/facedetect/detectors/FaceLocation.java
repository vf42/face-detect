package lv.rtu.dadi.facedetect.detectors;

import lv.rtu.dadi.facedetect.bitmaps.SubWindow;

/**
 * Container for face location data.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class FaceLocation {

    public final int x;
    public final int y;
    public final int w;
    public final int h;

    public FaceLocation(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public FaceLocation(SubWindow sw) {
        this.x = sw.x;
        this.y = sw.y;
        this.w = sw.w;
        this.h = sw.h;
    }

    /**
     * Return the overlap area size between two faces.
     * @param f2
     * @return
     */
    public int overlap(FaceLocation f2) {
        final int xOverlap = Math.max(0, Math.min(this.x + this.w, f2.x + f2.w) - Math.max(this.x, f2.x));
        final int yOverlap = Math.max(0, Math.min(this.y + this.h, f2.y + f2.h) - Math.max(this.y, f2.y));
        return xOverlap * yOverlap;
    }

    public int centerX() {
        return x + w / 2;
    }

    public int centerY() {
        return y + h / 2;
    }

}
