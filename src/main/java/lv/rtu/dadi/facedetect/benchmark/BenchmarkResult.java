package lv.rtu.dadi.facedetect.benchmark;


/**
 * Stores and prints benchmark results.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 *
 */
public class BenchmarkResult {

    public final int facesInBenchmark;
    public final int correctDetections;
    public final int falseDetections;
    public final int missedDetections;

    public final double detectionRate;
    public final double falseRejectionRate;
    public final double falseAcceptanceRate;

    public final double distToExemplary;

    public BenchmarkResult(int facesInBenchmark,
            int correctDetections, int falseDetections, int missedDetections) {
        this.facesInBenchmark = facesInBenchmark;
        this.correctDetections = correctDetections;
        this.falseDetections = falseDetections;
        this.missedDetections = missedDetections;
        this.detectionRate = correctDetections / (double) facesInBenchmark;
        this.falseRejectionRate = missedDetections / (double) correctDetections;
        this.falseAcceptanceRate = falseDetections / (double) correctDetections;
        this.distToExemplary = Math.sqrt(Math.pow(falseRejectionRate, 2) + Math.pow(falseAcceptanceRate, 2));
    }

    public void print() {
        System.out.println("------ Benchmark results ------");
        System.out.println(String.format("Faces in bench:   %d", facesInBenchmark));
        System.out.println(String.format("Correct detectns: %d", correctDetections));
        System.out.println(String.format("False detectns:   %d", falseDetections));
        System.out.println(String.format("Missed detectns:  %d", missedDetections));
        System.out.println(String.format("Detection rate:   %.3f", detectionRate));
        System.out.println(String.format("FRR:              %.3f", falseRejectionRate));
        System.out.println(String.format("FAR:              %.3f", falseAcceptanceRate));
        System.out.println(String.format("Dist to exempl:   %.3f", distToExemplary));
        System.out.println("-------------------------------");
    }

}
