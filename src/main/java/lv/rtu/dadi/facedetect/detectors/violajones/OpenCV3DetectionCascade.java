package lv.rtu.dadi.facedetect.detectors.violajones;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.haar.HaarLikeFeature;
import lv.rtu.dadi.facedetect.haar.HaarRectangle;
import lv.rtu.dadi.facedetect.haar.IntegralImage;

/**
 * The HAAR cascade using classifier cascade XML file format found in OpenCV 3.0RC1 distribution.
 * @author fedorovvadim
 *
 */
public class OpenCV3DetectionCascade implements DetectionCascade {

    private String stageType; // Always 'BOOST'.
    private String featureType; // ALways 'HAAR'.

    private int height;
    private int width;

    private int maxWeakCount;
    private int maxCatCount;
    private int stageNum;

    private final List<Stage> stages = new ArrayList<>();
    private final List<HaarLikeFeature> features = new ArrayList<>();

    private class Stage {
        int maxWeakCount;
        double stageThreshold;
        List<WeakClassifier> classifiers = new ArrayList<>();

        public boolean testWindow(IntegralImage ii, SubWindow sw, double scale) {
            double stageSum = 0.0;
            for (final WeakClassifier wcl : classifiers) {
                final HaarLikeFeature feature = features.get(wcl.featureId);
                final double featureResp = feature.getFeatureValue(ii, sw.x, sw.y, scale);
                if (featureResp <= wcl.threshold) {
                    stageSum += wcl.leafValues[0];
                } else {
                    stageSum += wcl.leafValues[1];
                }
            }
            if (stageSum <= stageThreshold) {
                return false;
            } else {
                return true;
            }
        }

    }

    private class WeakClassifier {
        int leftNode;
        int rightNode;
        int featureId;
        double threshold;
        double[] leafValues;
    }

    @Override
    public boolean testWindow(IntegralImage ii, SubWindow sw) {
        int idx = 0;
        final double scale = sw.w / (double) this.width; // Assuming fixed aspect ratio.
        for (final Stage stage : stages) {
            if (!stage.testWindow(ii, sw, scale)) {
                return false;
            }
            if (++idx > 36) break;
        }
        return true;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /*
     * Parsing code below.
     */

    public OpenCV3DetectionCascade(InputStream source) throws XMLStreamException  {
        parseCascade(source);
    }

    /**
     * Parse the whole cascade.
     * @param source
     * @throws XMLStreamException
     */
    private void parseCascade(InputStream source) throws XMLStreamException  {
        final XMLInputFactory factory = XMLInputFactory.newInstance();
        final XMLStreamReader reader = factory.createXMLStreamReader(source);
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "stages":
                    parseStages(reader);
                    break;
                case "features":
                    parseFeatures(reader);
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "stageType":
                    this.stageType = tagText;
                    break;
                case "featureType":
                    this.featureType = tagText;
                    break;
                case "width":
                    this.width = Integer.parseInt(tagText);
                    break;
                case "height":
                    this.height= Integer.parseInt(tagText);
                    break;
                case "maxWeakCount":
                    this.maxWeakCount = Integer.parseInt(tagText);
                    break;
                case "maxCatCount":
                    this.maxCatCount = Integer.parseInt(tagText);
                    break;
                case "stageNum":
                    this.stageNum = Integer.parseInt(tagText);
                    break;
                }
            }
        }
    }

    /**
     * Parse the <stages> section.
     * @param reader
     * @throws XMLStreamException
     */
    private void parseStages(XMLStreamReader reader) throws XMLStreamException {
        Stage currStage = null;
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "_":
                    currStage = new Stage(); // Start new stage.
                    break;
                case "weakClassifiers":
                    parseClassifiers(reader, currStage);
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "stages":
                    return; // Get back.
                case "_":
                    stages.add(currStage);
                    break;
                case "maxWeakCount":
                    currStage.maxWeakCount = Integer.parseInt(tagText);
                    break;
                case "stageThreshold":
                    currStage.stageThreshold = Double.parseDouble(tagText);
                    break;
                }
                break;
            }
        }
    }

    /**
     * Parse the classifiers for the stage.
     * @param source
     * @param currStage
     * @throws XMLStreamException
     */
    private void parseClassifiers(XMLStreamReader reader, Stage currStage) throws XMLStreamException {
        WeakClassifier currClassifier = null;
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "_":
                    currClassifier = new WeakClassifier();
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "weakClassifiers":
                    return; // Get back.
                case "_":
                    currStage.classifiers.add(currClassifier);
                    break;
                case "internalNodes":
                    final String[] split1 = tagText.split(" ");
                    currClassifier.leftNode = Integer.parseInt(split1[0]);
                    currClassifier.rightNode = Integer.parseInt(split1[1]);
                    currClassifier.featureId = Integer.parseInt(split1[2]);
                    currClassifier.threshold = Double.parseDouble(split1[3]);
                    break;
                case "leafValues":
                    final String[] split2 = tagText.split(" ");
                    currClassifier.leafValues =
                            Stream.of(split2).mapToDouble(Double::parseDouble).toArray();
                    break;
                }
                break;
            }
        }
    }

    /**
     * Parse the <features> section.
     * @param reader
     * @throws XMLStreamException
     */
    private void parseFeatures(XMLStreamReader reader) throws XMLStreamException {
        HaarRectangle[] rects = null;
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "_":
                    break;
                case "rects":
                    rects = parseRects(reader);
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "features":
                    return; // Get back.
                case "_":
                    features.add(new HaarLikeFeature(rects, this.width, this.height));
                    break;
                }
                break;
            }
        }
    }

    /**
     * Parse the <rects> section.
     * @param reader
     * @throws XMLStreamException
     */
    private HaarRectangle[] parseRects(XMLStreamReader reader) throws XMLStreamException {
        final List<HaarRectangle> rects = new LinkedList<>();
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "rects":
                    return rects.toArray(new HaarRectangle[0]); // Get back.
                case "_":
                    final String[] split = tagText.split(" ");
                    final int x = Integer.parseInt(split[0]);
                    final int y = Integer.parseInt(split[1]);
                    final int w = Integer.parseInt(split[2]);
                    final int h = Integer.parseInt(split[3]);
                    rects.add(new HaarRectangle(
                            x, y, x + w, y + h,
                            Double.parseDouble(split[4])));
                    break;
                }
                break;
            }
        }
        // We should never get here, actually.
        throw new RuntimeException("Error in rect parsing!");
    }

}
