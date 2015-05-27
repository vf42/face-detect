package lv.rtu.dadi.facedetect;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BufferedImageFactory;


/**
 * Various utility methods for image operations.
 * @author Vadim Fedorov <vadim.fedorov@gmail.com>
 */
public class ImageUtils {

    public static class ManagedImageBufferedImageFactory implements BufferedImageFactory {

        @Override
        public BufferedImage getColorBufferedImage(final int width, final int height, final boolean hasAlpha) {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        }

        @Override
        public BufferedImage getGrayscaleBufferedImage(final int width, final int height, final boolean hasAlpha) {
            return getColorBufferedImage(width, height, hasAlpha);
        }
    }

    /**
     * Read an image from the file.
     * @param fname
     * @return
     * @throws ImageReadException
     * @throws IOException
     */
    public static BufferedImage readImage(String fname) throws ImageReadException, IOException {
        return readImage(new File(fname));
    }

    public static BufferedImage readImage(File file) throws ImageReadException, IOException {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put(ImagingConstants.BUFFERED_IMAGE_FACTORY, new ManagedImageBufferedImageFactory());
        final BufferedImage image = Imaging.getBufferedImage(file, params);
        return image;
    }

}
