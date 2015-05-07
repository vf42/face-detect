package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.List;

import lv.rtu.dadi.facedetect.ImageScanVisualizer;
import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.FaceMerger;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.DetectionCascade;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.StupidDetectionCascade;
import lv.rtu.dadi.facedetect.detectors.violajones.scanners.LinearExhaustiveScanner;
import lv.rtu.dadi.facedetect.detectors.violajones.scanners.SubWindowScanner;

/**
 * Implementation of Viola-Jones face detection, using the prepared cascade.
 * @author fedorovvadim
 *
 */
public class SimpleVJFaceDetector implements FaceDetector {

    private final DetectionCascade cascade;


    private ImageScanVisualizer visual;

    public SimpleVJFaceDetector() {
        // Init detection cascade.
        this.cascade = new StupidDetectionCascade();
    }

    public SimpleVJFaceDetector(DetectionCascade cascade) {
        this.cascade = cascade;
    }

    /**
     * Extracted to separate method so it may be easily overrided.
     * @param scene
     * @return
     */
    protected SubWindowScanner getScanner(ViolaJonesContext context) {
        return new LinearExhaustiveScanner(this, context);
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        final IntegralImage integralImg = new IntegralImage(scene);
        final ViolaJonesContext context = new ViolaJonesContext(scene, integralImg, cascade.getFeatureCount());
        final SubWindowScanner scanner = getScanner(context);
        int prevWinSize = -1;
        while (scanner.hasNext()) {
            final SubWindow sw = scanner.next();
            if (sw.w != prevWinSize) {
                context.stats.addWindowSize(sw.w);
                prevWinSize = sw.w;
            }
            context.stats.addWindow();
            if (visual != null) {
                visual.setWindow(sw);
            }
            if (cascade.testWindow(integralImg, sw, context.stats)) {
                final FaceLocation fl = new FaceLocation(sw);
                context.rawResult.add(fl);
                context.stats.addFace();
                if (visual != null) {
                    visual.addFace(fl);
                }
            }
        }
        final List<FaceLocation> mergedResult = FaceMerger.faceCenterMerge(context.rawResult);
        context.stats.setMergedFaces(mergedResult.size());
        if (Settings.DEBUG) {
            context.stats.printStats();
        }
        return mergedResult;
    }

    public ImageScanVisualizer getVisual() {
        return visual;
    }

    @Override
    public void setVisual(ImageScanVisualizer visual) {
        this.visual = visual;
        cascade.setVisual(visual);
    }

    public DetectionCascade getCascade() {
        return cascade;
    }


}
