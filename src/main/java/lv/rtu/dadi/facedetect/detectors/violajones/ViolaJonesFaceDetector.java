package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.haar.FourRectHLF;
import lv.rtu.dadi.facedetect.haar.HaarLikeFeature;
import lv.rtu.dadi.facedetect.haar.ThreeRectHorizontalHLF;
import lv.rtu.dadi.facedetect.haar.TwoRectHorizontalHLF;
import lv.rtu.dadi.facedetect.haar.TwoRectVerticalHLF;

/**
 * Implementation of Viola-Jones face detection, without learning phase.
 * @author fedorovvadim
 *
 */
public class ViolaJonesFaceDetector implements FaceDetector {

    private final DetectionCascade cascade;

    public ViolaJonesFaceDetector() {
        // Init detection cascade.
        this.cascade = new DetectionCascade();
    }

    /**
     * Weak classifier that will either accept or reject particular window.
     * @author fedorovvadim
     *
     */
    public class WeakClassifier {
        private final HaarLikeFeature feature;
        private final double threshold;
        private final int parity;

        public WeakClassifier(HaarLikeFeature feature, double threshold, int parity) {
            this.feature = feature;
            this.threshold = threshold;
            if (Math.abs(parity) != 1) {
                throw new RuntimeException("Parity should be either 1 or -1.");
            }
            this.parity = parity;
        }

        public boolean testWindow(IntegralImage ii, SubWindow sw) {
            final double[][] featureValues = feature.regionScan(ii, sw.x, sw.y, sw.w, sw.h);
            // Try to find value that will pass the test.
            double min = featureValues[0][0];
            double max = featureValues[0][0];
            for (int i = 0; i < featureValues.length; i++) {
                for (int j = 0; j < featureValues[0].length; j++) {
                    if (featureValues[i][j] < min) min = featureValues[i][j];
                    if (featureValues[i][j] > max) max = featureValues[i][j];
                    if (parity * featureValues[i][j] < parity * threshold) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * A cascade of classifiers that will test if a sub-window may contain a face.
     * @author fedorovvadim
     *
     */
    public class DetectionCascade {
        private final List<WeakClassifier> cascade;

        public DetectionCascade() {
            // Statically adding 4 features - all must be present in the window.
            cascade = new LinkedList<>();
            cascade.add(new WeakClassifier(new TwoRectVerticalHLF(16, 6), 5, -1));
            cascade.add(new WeakClassifier(new ThreeRectHorizontalHLF(12, 8), -25, 1));
            cascade.add(new WeakClassifier(new TwoRectHorizontalHLF(6, 16), 5, -1));
            cascade.add(new WeakClassifier(new FourRectHLF(12, 12), 7, -1));
        }

        public boolean testWindow(IntegralImage ii, SubWindow sw) {
            for (final WeakClassifier wc : cascade) {
                if (!wc.testWindow(ii, sw)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        final List<FaceLocation> rawResult = new LinkedList<>();
        final IntegralImage integralImg = new IntegralImage(scene);
        // Perform the scan.
        final SubWindow sw = new SubWindow(0, 0, 24, 24);
        for ( ; sw.x < integralImg.getWidth() - sw.w ; sw.x++) {
            for (sw.y = 0 ; sw.y < integralImg.getHeight() - sw.h ; sw.y++) {
                if (cascade.testWindow(integralImg, sw)) {
                    rawResult.add(new FaceLocation(sw));
                }
            }
        }
        final List<FaceLocation> mergedResult = mergeFaces(rawResult);
        if (Settings.DEBUG) {
            System.out.println("Initial faces: " + rawResult.size());
            System.out.println("Merged faces: " + mergedResult.size());
        }
        return mergedResult;
    }

    /**
     * Perform the merge of overlapping faces to reduce result count.
     * @param originalFaces
     * @return
     */
    private List<FaceLocation> mergeFaces(List<FaceLocation> originalFaces) {
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
                    return 0;
                }
            });
            for (int j = i+1; j < allFaces.length; j++) {
                final int overlap = curr.overlap(allFaces[j]);
                if (overlap >= 72) { // XXX: Should we have some threshold?
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
