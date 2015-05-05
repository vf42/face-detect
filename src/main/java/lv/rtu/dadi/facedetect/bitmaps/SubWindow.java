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

    @Override
    public String toString() {
        return new StringBuilder(Integer.toString(x)).append(' ')
            .append(Integer.toString(y)).append(' ')
            .append(Integer.toString(w)).append(' ')
            .append(Integer.toString(h)).toString();
    }
}