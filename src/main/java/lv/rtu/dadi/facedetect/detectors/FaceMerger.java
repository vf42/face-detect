package lv.rtu.dadi.facedetect.detectors;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.bitmaps.SubWindow;

/**
 * Merge the resulting faces in order to reduce face count.
 * @author fedorovvadim
 *
 */
public class FaceMerger {

    public FaceMerger() {
    }


    /**
     * Very straightforward merge based on face (x0,y0) point proximity.
     * @param originalFaces
     * @return
     */
    public static List<FaceLocation> simpleMerge(List<FaceLocation> originalFaces) {
        final List<FaceLocation> mergedFaces = new LinkedList<>();
        // Sort faces by distance to the edge.
        final FaceLocation[] allFaces = originalFaces.toArray(new FaceLocation[0]);
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

    /**
     * Merge the faces based on proximity of their centers.
     * @param originalFaces
     * @return
     */
    public static List<FaceLocation> faceCenterMerge(List<FaceLocation> originalFaces) {
        final List<FaceLocation> mergedFaces = new LinkedList<>();
//        Collections.shuffle(originalFaces);
        final FaceLocation[] allFaces = originalFaces.toArray(new FaceLocation[0]);
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
            while (++currIdx < allFaces.length
                    && centerDistance(currFace, allFaces[currIdx]) < getCenterThreshold(mergedLocation)) {
                final FaceLocation nextFace = allFaces[currIdx];
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
            }
            mergedFaces.add(new FaceLocation(mergedLocation));
        }
        return mergedFaces;
    }

    /**
     * Get the threshold value for the current sub-window.
     * @param sw
     * @return
     */
    private static double getCenterThreshold(SubWindow sw) {
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
    private static double centerDistance(FaceLocation f1, FaceLocation f2) {
        return Math.sqrt(Math.pow(f1.centerX() - f2.centerX(), 2)
                + Math.pow(f1.centerY() - f2.centerY(), 2));
    }

}
