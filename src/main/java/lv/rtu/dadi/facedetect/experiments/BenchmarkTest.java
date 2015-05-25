package lv.rtu.dadi.facedetect.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.benchmark.FaceDetectorBenchmark;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.mergers.FaceCenterMerger;
import lv.rtu.dadi.facedetect.detectors.violajones.OptimalVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.OpenCV3DetectionCascade;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

import org.apache.commons.imaging.ImageReadException;

public class BenchmarkTest {

    private final static String CASCADE_FILE =
            "data\\cascades\\haarcascade_frontalface_alt.xml";

    public static void main(String[] args) throws XMLStreamException, ImageReadException, IOException {
        final long startTime = System.nanoTime();

        final FaceDetectorBenchmark bench =
                new FaceDetectorBenchmark("data/benchmarks/mit-cmu_test.xml", false);

        final FaceDetector detector1 = new SimpleVJFaceDetector(
                new OpenCV3DetectionCascade(
                        new FileInputStream(CASCADE_FILE)),
                new FaceCenterMerger(3, false));
//        final FaceDetector detector = new SmartScanVJFaceDetector(
//                new OpenCV3DetectionCascade(
//                        new FileInputStream(CASCADE_FILE)),
//                new FaceCenterMerger(8));
        final FaceDetector detector3 = new FaceDetector() {
            private final FaceDetector detector =
                    new SimpleVJFaceDetector(
                        new OpenCV3DetectionCascade(
                                new FileInputStream(CASCADE_FILE)),
                        new FaceCenterMerger(8, false));
            private final GrayscaleFilter preproc = GrayscaleFilterFactory.getFilterChain(
                    GrayscaleFilterFactory.getGammaCorrection(1.0, 1.4),
                    GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                    GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2));


            @Override
            public List<FaceLocation> detectFaces(GrayscaleImage scene) {
//                return detector.detectFaces(scene);
                return detector.detectFaces(preproc.apply(scene));
            }

        };
        final FaceDetector detector = new OptimalVJFaceDetector();
        bench.testDetector(detector);

        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println(String.format("Processing time: %.3fs", totalTime));
    }

}
