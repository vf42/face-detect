package lv.rtu.dadi.facedetect.detectors.violajones;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ViolaJonesStats {

    private int testedWindows = 0;
    private final List<Integer> windowSizes = new ArrayList<Integer>();

    private int testedFeatures = 0;
    private int foundFaces = 0;
    private int mergedFaces = 0;

    public final int featureCount;
    public final int[] featureStats; // How many times particular feature was tested.

    public ViolaJonesStats(int featureCount) {
        this.featureCount = featureCount;
        this.featureStats = new int[featureCount];
    }

    public void addWindow() {
        ++testedWindows;
    }

    public void addWindowSize(int size) {
        windowSizes.add(size);
    }

    public void addFeature(int featureId) {
        ++testedFeatures;
        ++featureStats[featureId];
    }

    public void addFace() {
        ++foundFaces;
    }

    public void setMergedFaces(int mergedFaces) {
        this.mergedFaces = mergedFaces;
    }

    public void printStats() {
        System.out.println("------- Algorithm stats -------");
        System.out.println(String.format("Tested windows:   %d", testedWindows));
        System.out.println(String.format("Window sizes: %s", StringUtils.join(windowSizes.toArray(), ", ")));
        System.out.println(String.format("Tested features:  %d", testedFeatures));
        System.out.println(String.format("Faces found:      %d", foundFaces));
        System.out.println(String.format("Merged faces:     %d", mergedFaces));
        System.out.println("-------------------------------");
    }

}
