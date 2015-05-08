package lv.rtu.dadi.facedetect.benchmark;


public class BenchmarkResult {

    public final int facesInBenchmark;
    public final int correctDetections;
    public final int falseDetections;
    // % of correctly detected faces.
    public final double detectionRate;
    // Rate of false detections to succesful detections.
    public final double falseDetectionRate;
    public final double distToExemplary;

    public BenchmarkResult(int facesInBenchmark, int correctDetections, int falseDetections,
            double detectionRate, double falseDetectionRate) {
        this.facesInBenchmark = facesInBenchmark;
        this.correctDetections = correctDetections;
        this.falseDetections = falseDetections;
        this.detectionRate = detectionRate;
        this.falseDetectionRate = falseDetectionRate;
        this.distToExemplary = Math.sqrt(Math.pow(1.0 - detectionRate, 2) + Math.pow(falseDetectionRate, 2));
    }

    public void print() {
        System.out.println("------ Benchmark results ------");
        System.out.println(String.format("Faces in bench:   %d", facesInBenchmark));
        System.out.println(String.format("Correct detectns: %d", correctDetections));
        System.out.println(String.format("False detections: %d", falseDetections));
        System.out.println(String.format("Detection rate:   %.3f", detectionRate));
        System.out.println(String.format("False det rate:   %.3f", falseDetectionRate));
        System.out.println(String.format("Dist to exempl:   %.3f", distToExemplary));
        System.out.println("-------------------------------");
    }

}
