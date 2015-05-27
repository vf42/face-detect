package lv.rtu.dadi.facedetect.haar;


/**
 * First two-rect feature.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public final class TwoRectHorizontalHLF extends HaarLikeFeature {

    public TwoRectHorizontalHLF(int width, int height) {
        super(new HaarRectangle[] {
                new HaarRectangle(0, 0, width/2, height, -1.0),
                new HaarRectangle(width/2, 0, width, height, 1.0)
        });
    }

}
