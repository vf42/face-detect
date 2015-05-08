package lv.rtu.dadi.facedetect.detectors.mergers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.detectors.FaceLocation;

/**
 * Stupid and ineffective merger.
 * @author fedorovvadim
 *
 */
public class SimpleFaceMerger implements FaceMerger {

    public SimpleFaceMerger() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<FaceLocation> mergeFaces(List<FaceLocation> rawResult) {
        final List<FaceLocation> mergedFaces = new LinkedList<>();
        // Sort faces by distance to the edge.
        final FaceLocation[] allFaces = rawResult.toArray(new FaceLocation[0]);
        Arrays.sort(allFaces, new Comparator<FaceLocation>() {
            @Override
            public int compare(FaceLocation o1, FaceLocation o2) {
                final double d1 = Math.sqrt(Math.pow(o1.x, 2) + Math.pow(o1.y, 2));
                final double d2 = Math.sqrt(Math.pow(o2.x, 2) + Math.pow(o2.y, 2));
                return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
            }
        });
        // Pass through the faces and merge them.
        for (int i = 0; i < allFaces.length - 1; i++) {
            FaceLocation curr = allFaces[i];
            final FaceLocation finalCurr = curr;
            // Sort all the following faces by the distance to current face.
            Arrays.sort(allFaces, i + 1, allFaces.length - 1, new Comparator<FaceLocation>() {
                @Override
                public int compare(FaceLocation o1, FaceLocation o2) {
                    final double d1 = Math.sqrt(Math.pow(o1.x - finalCurr.x, 2) + Math.pow(o1.y - finalCurr.y, 2));
                    final double d2 = Math.sqrt(Math.pow(o2.x - finalCurr.x, 2) + Math.pow(o2.y - finalCurr.y, 2));
                    return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
                }
            });
            for (int j = i+1; j < allFaces.length; j++) {
                final int overlap = curr.overlap(allFaces[j]);
                if (overlap >= (curr.w * curr.h / 4)) {
                    ++i; // We'll skip this face as it gets merged.
                    final int x1 = Math.min(curr.x, allFaces[i].x);
                    final int y1 = Math.min(curr.y, allFaces[i].y);
                    final int x2 = Math.max(curr.x + curr.w, allFaces[i].x + allFaces[i].w);
                    final int y2 = Math.max(curr.y + curr.h, allFaces[i].y + allFaces[i].h);
                    curr = new FaceLocation(x1, y1, x2 - x1, y2 - y1);
                }
            }
            mergedFaces.add(curr);
        }
        return mergedFaces;
    }

}
