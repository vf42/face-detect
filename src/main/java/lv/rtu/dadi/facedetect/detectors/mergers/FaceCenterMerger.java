package lv.rtu.dadi.facedetect.detectors.mergers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;

/**
 * Merges the faces that have same center.
 * @author fedorovvadim
 *
 */
public class FaceCenterMerger implements FaceMerger {

    private final int quorum; // How many faces should be detected in a location for success.
    private final boolean narrowestMatch;

    public FaceCenterMerger() {
        this(1);
    }

    public FaceCenterMerger(int quorum) {
        this(quorum, false);
    }

    public FaceCenterMerger(int quorum, boolean narrowestMatch) {
        this.quorum = quorum;
        this.narrowestMatch = narrowestMatch;
    }

    @Override
    public List<FaceLocation> mergeFaces(List<FaceLocation> rawResult) {
        final List<FaceLocation> mergedFaces = new LinkedList<>();
//      Collections.shuffle(originalFaces);
      final FaceLocation[] allFaces = rawResult.toArray(new FaceLocation[0]);
      int currIdx = 0;
      while (currIdx < allFaces.length) {
          final FaceLocation currFace = allFaces[currIdx];
          // Sort all the following faces by center distance.
          if (currIdx + 1 < allFaces.length - 1) {
              Arrays.sort(allFaces, currIdx + 1, allFaces.length, new Comparator<FaceLocation>() {
                  @Override
                  public int compare(FaceLocation o1, FaceLocation o2) {
                      final double d1 = centerDistance(o1, currFace);
                      final double d2 = centerDistance(o2, currFace);
                      return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
                  }
              });
          }
          // Merge near-located faces to this one.
          final SubWindow mergedLocation = new SubWindow(currFace.x, currFace.y, currFace.w, currFace.h);
          int facesAtLocation = 1;
          while (++currIdx < allFaces.length
                  && centerDistance(currFace, allFaces[currIdx]) < getCenterThreshold(mergedLocation)) {
              ++facesAtLocation;
              final FaceLocation nextFace = allFaces[currIdx];
              if (!narrowestMatch) {
                  if (nextFace.x < mergedLocation.x) {
                      mergedLocation.x = (mergedLocation.x + nextFace.x) / 2;
                  }
                  if (nextFace.y < mergedLocation.y) {
                      mergedLocation.y = (mergedLocation.y + nextFace.y) / 2;
                  }
                  final int x1 = nextFace.x + nextFace.w;
                  final int y1 = nextFace.y + nextFace.h;
                  if (x1 > mergedLocation.x + mergedLocation.w) {
                      mergedLocation.w = (mergedLocation.w + x1 - mergedLocation.x) / 2;
                  }
                  if (y1 > mergedLocation.y + mergedLocation.h) {
                      mergedLocation.h = (mergedLocation.h + y1 - mergedLocation.y) / 2;
                  }
              } else if (nextFace.w < mergedLocation.w) {
                  mergedLocation.x = nextFace.x;
                  mergedLocation.y = nextFace.y;
                  mergedLocation.w = nextFace.w;
                  mergedLocation.h = nextFace.h;
              }
          }
          if (facesAtLocation >= quorum) {
              mergedFaces.add(new FaceLocation(mergedLocation));
          }
      }
      return mergedFaces;
    }

    /**
     * Get the threshold value for the current sub-window.
     * @param sw
     * @return
     */
    private double getCenterThreshold(SubWindow sw) {
        final int x0 = sw.x + (sw.w / 3);
        final int y0 = sw.y + (sw.h / 3);
        final int x1 = sw.x + (2*sw.w / 3);
        final int y1 = sw.y + (2*sw.h / 3);
        return Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
    }

    /**
     * Return the distance between centers of two faces.
     * @param f2
     * @return
     */
    private double centerDistance(FaceLocation f1, FaceLocation f2) {
        return Math.sqrt(Math.pow(f1.centerX() - f2.centerX(), 2)
                + Math.pow(f1.centerY() - f2.centerY(), 2));
    }

}
