package lv.rtu.dadi.facedetect.detectors.violajones;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import lv.rtu.dadi.facedetect.bitmaps.IntegralImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.haar.HaarLikeFeature;
import lv.rtu.dadi.facedetect.haar.HaarRectangle;

/**
 * The HAAR cascade using classifier cascade XML file format found in JViolaJones distribution.
 * @author fedorovvadim
 *
 */
public class OpenCV2DetectionCascade implements DetectionCascade {

    private int height;
    private int width;

    private final List<Stage> stages = new ArrayList<>();

    private class Stage {
        double stageThreshold;
        int parent;
        int next;
        List<Tree> trees = new ArrayList<>();

        boolean testWindow(IntegralImage ii, SubWindow sw, double scale) {
            double stageSum = 0.0;
            // Sum up tree values.
            for (final Tree tree : trees) {
                stageSum += tree.getTreeValue(ii, sw, scale);
            }
            return stageSum > stageThreshold;
        }
    }

    private class Tree {
        TreeNode root; // Assuming all trees having one node for now.

        double getTreeValue(IntegralImage ii, SubWindow sw, double scale) {
            return root.getNodeValue(ii, sw, scale);
        }
    }

    private class TreeNode {
        HaarLikeFeature feature;
        double threshold;
        double leftVal;
        double rightVal;

        double getNodeValue(IntegralImage ii, SubWindow sw, double scale) {
            final double featureResp = feature.getFeatureValue(ii, sw.x, sw.y, scale);
            if (featureResp <= threshold) {
                return leftVal;
            } else {
                return rightVal;
            }
        }
    }

    @Override
    public boolean testWindow(IntegralImage ii, SubWindow sw) {
        final int idx = 0;
        final double scale = sw.w / (double) this.width; // Assuming fixed aspect ratio.
        for (final Stage stage : stages) {
            if (!stage.testWindow(ii, sw, scale)) {
                return false;
            }
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
     * Only parsing code below.
     */

    public OpenCV2DetectionCascade(InputStream source) throws XMLStreamException  {
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
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "size":
                    final String[] split = tagText.split(" ");
                    this.width = Integer.parseInt(split[0]);
                    this.height= Integer.parseInt(split[1]);
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
                case "trees":
                    parseTrees(reader, currStage);
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
                case "stageThreshold":
                    currStage.stageThreshold = Double.parseDouble(tagText);
                    break;
                case "parent":
                    currStage.parent = Integer.parseInt(tagText);
                    break;
                case "next":
                    currStage.next = Integer.parseInt(tagText);
                    break;
                }
                break;
            }
        }
    }

    private void parseTrees(XMLStreamReader reader, Stage currStage) throws XMLStreamException {
        Tree currTree = null;
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "_":
                    if (currTree == null) {
                        // First <_>.
                        currTree = new Tree();
                    } else {
                        // Second <_>.
                        parseTreeNode(reader, currTree);
                    }
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "trees":
                    return; // Get back.
                case "_":
                    currStage.trees.add(currTree);
                    currTree = null;
                    break;
                }
                break;
            }
        }
    }

    private void parseTreeNode(XMLStreamReader reader, Tree currTree) throws XMLStreamException {
        final TreeNode currNode = new TreeNode();
        currTree.root = currNode;
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
                case "feature":
                    parseFeature(reader, currNode);
                    break;
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                tagText = reader.getText().trim();
                break;

            case XMLStreamConstants.END_ELEMENT:
                switch (reader.getLocalName()) {
                case "_":
                    return; // Get back.
                case "threshold":
                    currNode.threshold = Double.parseDouble(tagText);
                    break;
                case "left_val":
                    currNode.leftVal = Double.parseDouble(tagText);
                    break;
                case "right_val":
                    currNode.rightVal = Double.parseDouble(tagText);
                    break;
                }
                break;
            }
        }
    }

    /**
     * Parse the <feature> section.
     * @param reader
     * @throws XMLStreamException
     */
    private void parseFeature(XMLStreamReader reader, TreeNode currNode) throws XMLStreamException {
        HaarRectangle[] rects = null;
        String tagText = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch(event) {
            case XMLStreamConstants.START_ELEMENT:
                switch (reader.getLocalName()) {
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
                case "feature":
                    currNode.feature = new HaarLikeFeature(rects, width, height);
                    return; // Get back.
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
