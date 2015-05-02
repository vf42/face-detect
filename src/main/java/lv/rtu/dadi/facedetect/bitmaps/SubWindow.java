package lv.rtu.dadi.facedetect.bitmaps;

/**
 * Defines an area in the image.
 * @author fedorovvadim
 *
 */
public class SubWindow {
    public int x, y, w, h;
    public SubWindow(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}