package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.LinkedList;
import java.util.List;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.haar.FourRectHLF;
import lv.rtu.dadi.facedetect.haar.HaarLikeFeature;
import lv.rtu.dadi.facedetect.haar.ThreeRectHorizontalHLF;
import lv.rtu.dadi.facedetect.haar.TwoRectHorizontalHLF;
import lv.rtu.dadi.facedetect.haar.TwoRectVerticalHLF;

/**
 * A cascade of classifiers that will test if a sub-window may contain a face.
 * @author fedorovvadim
 *
 */
public class StupidDetectionCascade implements DetectionCascade {

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

    private final List<WeakClassifier> cascade;

    public StupidDetectionCascade() {
        // Statically adding 4 features - all must be present in the window.
        cascade = new LinkedList<>();
        cascade.add(new WeakClassifier(new TwoRectVerticalHLF(16, 6), 5, -1));
        cascade.add(new WeakClassifier(new ThreeRectHorizontalHLF(12, 8), -25, 1));
        cascade.add(new WeakClassifier(new TwoRectHorizontalHLF(6, 16), 5, -1));
        cascade.add(new WeakClassifier(new FourRectHLF(12, 12), 7, -1));
    }

    @Override
    public boolean testWindow(IntegralImage ii, SubWindow sw) {
        for (final WeakClassifier wc : cascade) {
            if (!wc.testWindow(ii, sw)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }
}