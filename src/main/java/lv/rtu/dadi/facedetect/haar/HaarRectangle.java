package lv.rtu.dadi.facedetect.haar;

public class HaarRectangle {

    public final int x0;
    public final int y0;
    public final int x1;
    public final int y1;

    public final double weight;

    public HaarRectangle(int x0, int y0, int x1, int y1, double weight) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
    }

}
