package lv.rtu.dadi.facedetect.experiments;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.benchmark.BenchmarkResult;
import lv.rtu.dadi.facedetect.benchmark.FaceDetectorBenchmark;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.violajones.OptimalVJFaceDetector;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

import org.apache.commons.imaging.ImageReadException;

public class BenchOwnVersions {


    private static BenchmarkResult testOptimal(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final FaceDetector detector = new OptimalVJFaceDetector();
        return bench.testDetector(detector);
    }

    private static BenchmarkResult testNoPreproc(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final OptimalVJFaceDetector detector = new OptimalVJFaceDetector();
        detector.setPreprocessor(GrayscaleFilterFactory.getFilterChain());
        return bench.testDetector(detector);
    }

    private static BenchmarkResult testHEOnly(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final OptimalVJFaceDetector detector = new OptimalVJFaceDetector();
        detector.setPreprocessor(GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization()));
        return bench.testDetector(detector);
    }

    private static BenchmarkResult testLinearsOnly(FaceDetectorBenchmark bench)
            throws ImageReadException, IOException, XMLStreamException {
        final OptimalVJFaceDetector detector = new OptimalVJFaceDetector();
        detector.setPreprocessor(GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2)));
        return bench.testDetector(detector);
    }

    public static void main(String[] args) throws XMLStreamException, ImageReadException, IOException {
        final long startTime = System.nanoTime();

        final FaceDetectorBenchmark bench =
                new FaceDetectorBenchmark("data/benchmarks/mit-cmu_newtest.xml", false);

        final BenchmarkResult resultOptimal = testOptimal(bench);
        final BenchmarkResult resultNoPreproc = testNoPreproc(bench);
        final BenchmarkResult resultHeOnly = testHEOnly(bench);
        final BenchmarkResult resultLinearsOnly = testLinearsOnly(bench);

        System.out.println("OPTIMAL");
        resultOptimal.print();
        System.out.println("NO PREPROC");
        resultNoPreproc.print();
        System.out.println("HE ONLY");
        resultHeOnly.print();
        System.out.println("LINEARS ONLY");
        resultLinearsOnly.print();


        final double totalTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println(String.format("Processing time: %.3fs", totalTime));
    }

}
