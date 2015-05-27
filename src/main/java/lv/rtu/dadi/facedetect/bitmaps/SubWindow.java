package lv.rtu.dadi.facedetect.bitmaps;

/**
 * Defines an area in the image.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
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

    @Override
    public SubWindow clone() {
        return new SubWindow(x, y, w, h);
    }
}