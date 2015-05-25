package lv.rtu.dadi.facedetect.detectors.violajones;

import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.mergers.FaceCenterMerger;
import lv.rtu.dadi.facedetect.detectors.violajones.cascades.OpenCV3DetectionCascade;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilter;
import lv.rtu.dadi.facedetect.filters.GrayscaleFilterFactory;

/**
 * Tuned implementation that provides optimal results.
 * @author fedorovvadim
 *
 */
public class OptimalVJFaceDetector extends SimpleVJFaceDetector {

    private final static String CASCADE_FILE = "cascades/haarcascade_frontalface_alt.xml";

    private final GrayscaleFilter preprocessor;

    public OptimalVJFaceDetector() throws FileNotFoundException, XMLStreamException {
        super(new OpenCV3DetectionCascade(
                                ClassLoader.getSystemClassLoader().getResourceAsStream(CASCADE_FILE)),
                        new FaceCenterMerger(3, false));
        this.preprocessor = GrayscaleFilterFactory.getFilterChain(
                GrayscaleFilterFactory.getHistogramEquialization(),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.HIFREQ_3X3_1),
                GrayscaleFilterFactory.getLinearFilter(GrayscaleFilterFactory.LOWFREQ_3X3_2));
    }

    @Override
    public List<FaceLocation> detectFaces(GrayscaleImage scene) {
        return super.detectFaces(getPreprocessed(scene));
    }

    @Override
    public GrayscaleImage getPreprocessed(GrayscaleImage scene) {
        return preprocessor.apply(scene);
    }

}
