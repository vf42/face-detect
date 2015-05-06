package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.LinkedList;
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
import lv.rtu.dadi.facedetect.detectors.violajones.scanners.LinearExhaustiveSubWindowScanner;
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
    private SubWindowScanner getScanner(GrayscaleImage scene) {
        return new LinearExhaustiveSubWindowScanner(this, scene);
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        final ViolaJonesStats stats = new ViolaJonesStats(cascade.getFeatureCount());
        final List<FaceLocation> rawResult = new LinkedList<>();
        final IntegralImage integralImg = new IntegralImage(scene);
        final SubWindowScanner scanner = getScanner(scene);
        int prevWinSize = -1;
        while (scanner.hasNext()) {
            final SubWindow sw = scanner.next();
            if (sw.w != prevWinSize) {
                stats.addWindowSize(sw.w);
                prevWinSize = sw.w;
            }
            stats.addWindow();
            if (visual != null) {
                visual.setWindow(sw);
            }
            if (cascade.testWindow(integralImg, sw, stats)) {
                final FaceLocation fl = new FaceLocation(sw);
                rawResult.add(fl);
                stats.addFace();
                if (visual != null) {
                    visual.addFace(fl);
                }
            }
        }
        final List<FaceLocation> mergedResult = FaceMerger.faceCenterMerge(rawResult);
        stats.setMergedFaces(mergedResult.size());
        if (Settings.DEBUG) {
            stats.printStats();
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
