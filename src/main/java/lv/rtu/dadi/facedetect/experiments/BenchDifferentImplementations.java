package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.benchmark.BenchmarkResult;
import lv.rtu.dadi.facedetect.benchmark.FaceDetectorBenchmark;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.OptimalVJFaceDetector;
import lv.rtu.dadi.facedetect.external.JViolaJonesDetector;
import lv.rtu.dadi.facedetect.external.OpenCVDetector;

import org.apache.commons.imaging.ImageReadException;

/**
 * Benchmark of own implementation vs jviolajones vs opencv.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class BenchDifferentImplementations {


    private static BenchmarkResult testOptimal(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final FaceDetector detector = new OptimalVJFaceDetector();
        return bench.testDetector(detector);
    }

    private static BenchmarkResult testJVJ(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final FaceDetector detector = new JViolaJonesDetector();
        return bench.testDetector(detector, true);
    }

    private static BenchmarkResult testOCV(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final FaceDetector detector = new OpenCVDetector();
        return bench.testDetector(detector, true);
    }


    public static void main(String[] args) throws XMLStreamException, ImageReadException, IOException {
        final long startTime = System.nanoTime();

        final FaceDetectorBenchmark bench =
                new FaceDetectorBenchmark("data/benchmarks/mit-cmu_abc.xml", false);

//        final BenchmarkResult resultOptimal = testOptimal(bench);
        final BenchmarkResult resultOCV = testOCV(bench);
//        final BenchmarkResult resultJVJ = testJVJ(bench);

//        System.out.println("OWN");
//        resultOptimal.print();
        System.out.println("OpenCV");
        resultOCV.print();
//        System.out.println("JViolaJones");
//        resultJVJ.print();


        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println(String.format("Processing time: %.3fs", totalTime));
    }

}
