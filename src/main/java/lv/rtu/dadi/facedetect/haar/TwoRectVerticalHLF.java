package lv.rtu.dadi.facedetect.haar;


/**
 * Second two-rect feature.
 * @author fedorovvadim
 *
 */
public class TwoRectVerticalHLF extends HaarLikeFeature {

    public TwoRectVerticalHLF(int width, int height) {
        super(new HaarRectangle[] {
                new HaarRectangle(0, 0, width, height/2, 1.0),
                new HaarRectangle(0, height/2, width, height, -1.0)
        });
    }

}
