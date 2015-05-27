package lv.rtu.dadi.facedetect.external;

import java.awt.image.BufferedImage;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;

/**
 * Stub implementation used to serve only as a container for file names.
 * @author fedorovvadim
 *
 */
public class FakeGrayscaleImage extends GrayscaleImage {

    private String filename = null;

    public String getFilename() {
        return filename;
    }

    private FakeGrayscaleImage(BufferedImage source) {
        super(source);
    }

    private FakeGrayscaleImage(int width, int height, double fill) {
        super(width, height, fill);
    }

    private FakeGrayscaleImage(double[][] pixels) {
        super(pixels);
    }

    private FakeGrayscaleImage(int width, int height) {
        super(width, height);
    }

    public FakeGrayscaleImage(String filename) {
        super(1, 1);
        this.filename = filename;
    }

}
