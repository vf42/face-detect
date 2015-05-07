package lv.rtu.dadi.facedetect.benchmark;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import lv.rtu.dadi.facedetect.ImageUtils;
import lv.rtu.dadi.facedetect.Settings;
import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;

import org.apache.commons.imaging.ImageReadException;

/**
 * Provides FRR, FAR and "distance to ideal" measuring for a FaceDetector,
 * using the provided XML configuration of face data.
 * @author fedorovvadim
 *
 */
public class FaceDetectorBenchmark {

    class BenchFile {
        String path;
        List<Point> faces = new ArrayList<Point>(); // Storing approximate centers of faces.
    }

    private final List<BenchFile> files;

    public FaceDetectorBenchmark(String confFile) {
        files = new LinkedList<>();
        try {
        parseBenchmarkConfig(confFile);
        } catch (final Exception ex) {
            throw new RuntimeException("Couldn't instantiate benchmark", ex);
        }
    }

    public BenchmarkResult testDetector(FaceDetector detector) throws ImageReadException, IOException {
        int totalFaces = 0;
        int correctDetections = 0;
        int wrongDetections = 0;
        int idx = 0;
        for (final BenchFile bf : files) {
            try {
                int fileCorrect = 0;
                int fileWrong = 0;
                final GrayscaleImage scene = new GrayscaleImage(ImageUtils.readImage(bf.path));
                final List<FaceLocation> detectorResult = detector.detectFaces(scene);
                for (final FaceLocation fl : detectorResult) {
                    if (!bf.faces.stream().anyMatch(
                            p -> p.x > fl.x && p.x < fl.x + fl.w && p.y > fl.y && p.y < fl.y + fl.h)) {
                        ++fileWrong;
                    }
                }
                for (final Point p : bf.faces) {
                    if (detectorResult.stream().anyMatch(
                            fl -> p.x > fl.x && p.x < fl.x + fl.w && p.y > fl.y && p.y < fl.y + fl.h)) {
                        ++fileCorrect;
                    }
                }

                totalFaces += bf.faces.size();
                correctDetections += fileCorrect;
                wrongDetections += fileWrong;

                if (Settings.DEBUG) {
                    System.out.println(bf.path);
                    System.out.println(String.format("%d / %d:\t%d %d %d",
                            ++idx, files.size(), bf.faces.size(), fileCorrect, fileWrong));
                }
            } catch (final Exception ex) {
                // Just skipping this file.
                ex.printStackTrace();
            }
        }
        final BenchmarkResult result = new BenchmarkResult(totalFaces,
                correctDetections / (double) totalFaces,
                wrongDetections / (double) correctDetections);
        if (Settings.DEBUG) {
            result.print();
        }
        return result;
    }

    /**
     * Parse the benchmark configuration from file.
     * @param confFile
     * @throws FileNotFoundException
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     */
    private void parseBenchmarkConfig(String confFile)
            throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        final String dir = new File(confFile).getParent() + "/";
        if (Settings.DEBUG) {
            System.out.println("Loading benchmark from " + confFile + " (" + dir + ")");
            System.out.println("Loading benchmark from " + confFile);
        }
        final XMLStreamReader reader =
                XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(confFile));
        String tagText = null;
        BenchFile currFile = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "file":
                    if (currFile != null) {
                        throw new RuntimeException("Nested file tags are not allowed!");
                    }
                    currFile = new BenchFile();
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "benchmark":
                    return;
                case "file":
                    files.add(currFile);
                    currFile = null;
                    break;
                case "path":
                    // Assuming relative paths.
                    currFile.path = dir + tagText;
                    break;
                case "face":
                    final String[] split = tagText.split("\\s");
                    currFile.faces.add(new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
                    break;
                }
                break;
            }
        }

    }

}
