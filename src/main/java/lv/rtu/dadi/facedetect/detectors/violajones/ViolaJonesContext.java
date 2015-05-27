package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.ArrayList;
import java.util.List;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;

/**
 * Some stuff that should be commonly available during the process.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class ViolaJonesContext {

    public final GrayscaleImage scene;
    public final IntegralImage integralImg;

    public final ViolaJonesStats stats;
    public final List<FaceLocation> rawResult = new ArrayList<>();

    public ViolaJonesContext(GrayscaleImage scene, IntegralImage integralImg, int featureCount) {
        this.scene = scene;
        this.integralImg = integralImg;
        this.stats = new ViolaJonesStats(featureCount);
    }

}
