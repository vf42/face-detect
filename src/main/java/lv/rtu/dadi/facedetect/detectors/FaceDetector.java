package lv.rtu.dadi.facedetect.detectors;

import java.util.List;

import lv.rtu.dadi.facedetect.ImageScanVisualizer;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;

/**
 * Interface for general face detection algorithm.
 * @author fedorovvadim
 *
 */
public interface FaceDetector {
    List<FaceLocation> detectFaces(GrayscaleImage scene);

    default void setVisual(ImageScanVisualizer visual) {

    }

    default GrayscaleImage getPreprocessed(GrayscaleImage scene) {
        return scene;
    }
}
