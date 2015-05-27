package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.benchmark.BenchmarkResult;
import lv.rtu.dadi.facedetect.benchmark.FaceDetectorBenchmark;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.OptimalVJFaceDetector;

import org.apache.commons.imaging.ImageReadException;

public class BenchSingle {

    public static void main(String[] args) throws ImageReadException, IOException, XMLStreamException {
        final long startTime = System.nanoTime();

        final FaceDetectorBenchmark bench =
                new FaceDetectorBenchmark("data/benchmarks/mit-cmu_abc.xml", false);

        final FaceDetector detector = new OptimalVJFaceDetector();
        final BenchmarkResult result = bench.testDetector(detector);

        System.out.println("RESULT");
        result.print();

        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println(String.format("Processing time: %.3fs", totalTime));
    }

}
