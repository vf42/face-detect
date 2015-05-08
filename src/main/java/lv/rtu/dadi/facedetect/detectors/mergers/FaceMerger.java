package lv.rtu.dadi.facedetect.detectors.mergers;

import java.util.List;

import lv.rtu.dadi.facedetect.detectors.FaceLocation;

/**
 * An interface for face detector result merge.
 * @author fedorovvadim
 *
 */
public interface FaceMerger {

    List<FaceLocation> mergeFaces(List<FaceLocation> rawResult);

}
