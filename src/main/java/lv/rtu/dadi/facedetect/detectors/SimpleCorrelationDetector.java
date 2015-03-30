package lv.rtu.dadi.facedetect.detectors;

import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.GlobalSettings;
import lv.rtu.dadi.facedetect.ImagePreviewWindow;
import lv.rtu.dadi.facedetect.ImagePreviewWindow.GrayscaleDrawMode;
import lv.rtu.dadi.facedetect.bitmaps.Bitmap;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleBitmap;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

/**
 * Performs detection using simple 2D corellation.
 *
 * @author fedorovvadim
 *
 */
public class SimpleCorrelationDetector implements FaceDetector {

    private final GrayscaleBitmap template;
    private final GrayscaleFilter nccFilter;
    private final double threshold; // NCC value threshold for face detection.

    public SimpleCorrelationDetector(GrayscaleBitmap template) {
        this(template, 0.5);
    }

    public SimpleCorrelationDetector(GrayscaleBitmap template, double threshold) {
        this.template = template;
        this.threshold = threshold;
        this.nccFilter = GrayscaleFilterFactory.getNCCFilter(template);
    }

    @Override
    public List<FaceLocation> detectFaces(Bitmap scene) {
        if (!(scene instanceof GrayscaleBitmap)) {
            throw new RuntimeException("Only Grayscale Bitmaps are supported!");
        }
        final List<FaceLocation> result = new LinkedList<FaceLocation>();
        final GrayscaleBitmap ncc = nccFilter.apply((GrayscaleBitmap) scene);
        if (GlobalSettings.DEBUG) {
            new ImagePreviewWindow("Template", template);
            new ImagePreviewWindow("Scene", (GrayscaleBitmap) scene);
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
