package lv.rtu.dadi.facedetect.detectors.violajones;

import lv.rtu.dadi.facedetect.detectors.violajones.cascades.DetectionCascade;
import lv.rtu.dadi.facedetect.detectors.violajones.scanners.SmartShrinkingScanner;
import lv.rtu.dadi.facedetect.detectors.violajones.scanners.SubWindowScanner;

public class SmartScanVJFaceDetector extends SimpleVJFaceDetector {

    public SmartScanVJFaceDetector() {
        // TODO Auto-generated constructor stub
    }

    public SmartScanVJFaceDetector(DetectionCascade cascade) {
        super(cascade);
    }

    @Override
    protected SubWindowScanner getScanner(ViolaJonesContext context) {
        return new SmartShrinkingScanner(this, context);
    }

}
