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

    public HaarRectangle scaled(double factor) {
        if (factor != 1.0) {
            return new HaarRectangle(
                    (int) Math.floor(x0 * factor),
                    (int) Math.floor(y0 * factor),
                    (int) Math.floor(x1 * factor),
                    (int) Math.floor(y1 * factor),
//                    (int) Math.floor(x0*factor + (x1 - x0) * factor),
//                    (int) Math.floor(y0*factor + (y1 - y0) * factor),
                    weight);
        } else {
            return this;
        }
    }

}
