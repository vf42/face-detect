package lv.rtu.dadi.facedetect.detectors;

import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImagePreviewWindow.GrayscaleDrawMode;
import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

/**
 * Performs detection using simple 2D corellation.
 *
 * @author fedorovvadim
 *
 */
public class SimpleCorrelationDetector implements FaceDetector {

    private final GrayscaleImage template;
    private final GrayscaleFilter nccFilter;
    private final double threshold; // NCC value threshold for face detection.

    public SimpleCorrelationDetector(GrayscaleImage template) {
        this(template, 0.5);
    }

    public SimpleCorrelationDetector(GrayscaleImage template, double threshold) {
        this.template = template;
        this.threshold = threshold;
        this.nccFilter = GrayscaleFilterFactory.getNCCFilter(template);
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        final List<FaceLocation> result = new LinkedList<FaceLocation>();
        final GrayscaleImage ncc = nccFilter.apply(scene);
        if (Settings.DEBUG) {
            new ImagePreviewWindow("Template", template);
            new ImagePreviewWindow("Scene", scene);
            new ImagePreviewWindow("NCC map", ncc, GrayscaleDrawMode.EXTENDED_HEIGHTMAP);
        }
        for (int x = 0; x < ncc.getWidth(); x++) {
            for (int y = 0; y < ncc.getHeight(); y++) {
                if (ncc.pixels[x][y] > threshold) {
                    result.add(new FaceLocation(x, y, template.getWidth(), template.getHeight()));
                }
            }
        }
        return result;
    }

}
