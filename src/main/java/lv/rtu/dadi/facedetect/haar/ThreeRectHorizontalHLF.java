package lv.rtu.dadi.facedetect.haar;


/**
 * Three-rect feature.
 * @author fedorovvadim
 *
 */
public class ThreeRectHorizontalHLF extends HaarLikeFeature {

    public ThreeRectHorizontalHLF(int width, int height) {
        super(new HaarRectangle[] {
                new HaarRectangle(0, 0, width/3, height, -1.0),
                new HaarRectangle(width/3, 0, 2*width/3, height, 1.0),
                new HaarRectangle(2*width/3, 0, width, height, -1.0),
        });
    }

}
