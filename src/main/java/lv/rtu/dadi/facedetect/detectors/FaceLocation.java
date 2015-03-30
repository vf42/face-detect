package lv.rtu.dadi.facedetect.detectors;

/**
 * Container for face location data.
 * @author fedorovvadim
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

}
