package lv.rtu.dadi.facedetect.benchmark;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import lv.rtu.dadi.facedetect.benchmark.FaceDetectorBenchmark.BenchFile;

/**
 * Converts the ground truth face data from MIT-CMU to own XML benchmark format.
 * http://vasc.ri.cmu.edu/idb/images/face/frontal_images/list.html
 *
 * @author fedorovvadim
 *
 */
public class MitCmu2Benchmark {

    public static void main(String[] args) throws IOException, XMLStreamException, FactoryConfigurationError {
        if (args.length < 2) {
            System.out.println("Provide IN and OUT file names!");
            return;
        }
        final File inf = new File(args[0]);
        final File outf = new File(args[1]);
        if (!inf.exists() || !inf.isFile()) {
            System.out.println("IN file does not exist!");
        }
        final String pathPrefix = args.length > 2 ? args[2] : "";
        // Parse ground truth data.
        final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inf)));
        String line;
        final Map<String, BenchFile> benchFiles = new LinkedHashMap<>();
        while ((line = in.readLine()) != null) {
            // filename left-eye right-eye nose left-corner-mouth center-mouth right-corner-mouth
            final String split[] = line.split(" ");
            final String fname = split[0];
            final double lx = Double.parseDouble(split[1]);
            final double ly = Double.parseDouble(split[2]);
            final double rx = Double.parseDouble(split[3]);
            final double ry = Double.parseDouble(split[4]);
            final int cx = (int) Math.round((lx + rx) / 2);
            final int cy = (int) Math.round((ly + ry) / 2);
            if (!benchFiles.containsKey(fname)) {
                benchFiles.put(fname, new BenchFile());
                benchFiles.get(fname).path = pathPrefix.concat(fname);
            }
            benchFiles.get(fname).faces.add(new Point(cx, cy));
        }
        // Save as own benchmark config.
        final XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(outf));
        writer.writeStartDocument();
        writer.writeStartElement("benchmark");
        for (final BenchFile bf : benchFiles.values()) {
            writer.writeStartElement("file");
            writer.writeStartElement("path");
            writer.writeCharacters(bf.path);
            writer.writeEndElement();
            for (final Point p : bf.faces) {
                writer.writeStartElement("face");
                writer.writeCharacters(String.format("%d %d", p.x, p.y));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndDocument();
    }

}
