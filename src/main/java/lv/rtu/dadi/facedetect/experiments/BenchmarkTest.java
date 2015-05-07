package lv.rtu.dadi.facedetect.experiments;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.benchmark.FaceDetectorBenchmark;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.SimpleVJFaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.OpenCV3DetectionCascade;

import org.apache.commons.imaging.ImageReadException;

public class BenchmarkTest {

    private final static String CASCADE_FILE =
            "data\\cascades\\haarcascade_frontalface_alt.xml";

    public static void main(String[] args) throws XMLStreamException, ImageReadException, IOException {
        final long startTime = System.nanoTime();

        final FaceDetectorBenchmark bench = new FaceDetectorBenchmark("data/benchmarks/mit-cmu_test-low.xml");
        final FaceDetector detector = new SimpleVJFaceDetector(
                new OpenCV3DetectionCascade(
                        new FileInputStream(CASCADE_FILE)));
        bench.testDetector(detector);

        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println(String.format("Processing time: %.3fs", totalTime));
    }

}
