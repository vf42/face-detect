package lv.rtu.dadi.facedetect.detectors;

import java.util.List;

import lv.rtu.dadi.facedetect.bitmaps.Bitmap;

/**
 * Interface for general face detection algorithm.
 * @author fedorovvadim
 *
 */
public interface FaceDetector {
    List<FaceLocation> detectFaces(Bitmap scene);
}
