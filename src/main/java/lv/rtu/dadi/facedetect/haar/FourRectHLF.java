package lv.rtu.dadi.facedetect.haar;


/**
 * Four-rect Haar-like feature.
 * @author fedorovvadim
 *
 */
public class FourRectHLF extends HaarLikeFeature {

    public FourRectHLF(int width, int height) {
        super(new HaarRectangle[] {
                new HaarRectangle(0, 0, width/2, height/2, -1.0),
                new HaarRectangle(width/2, 0, width, height/2, 1.0),
                new HaarRectangle(0, height/2, width/2, height, 1.0),
                new HaarRectangle(width/2, height/2, width, height, -1.0)
        });
    }

}
