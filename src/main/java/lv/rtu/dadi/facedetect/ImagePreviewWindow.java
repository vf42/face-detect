package lv.rtu.dadi.facedetect;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleBitmap;
import lv.rtu.dadi.facedetect.bitmaps.RgbBitmap;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;

/**
 * Simple window to preview an image.
 * @author fedorovvadim
 *
 */
public class ImagePreviewWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Select how to display RGB bitmap.
     */
    public static enum RgbDrawMode {
        RGB, R, G, B, I;
    }

    /**
     * Select how to display Grayscale bitmap.
     */
    public static enum GrayscaleDrawMode {
        SIMPLE_POSITIVE, // Draw grayscale image using bitmap values [0..-1].
        EXTENDED_HEIGHTMAP // Draw color image using bitmap values [-1..1] with Red/Blue shift for color values.
    }

    /**
     * Drawable component for image.
     * @author fedorovvadim
     *
     */
    private static class ImageCanvas extends Canvas {
        private static final long serialVersionUID = 1L;

        private final BufferedImage image;
        private final List<FaceLocation> faces;

        public ImageCanvas(BufferedImage image) {
            this.image = image;
            this.faces = null;
        }

        public ImageCanvas(GrayscaleBitmap bmp, GrayscaleDrawMode mode) {
            this(bmp, mode, null);
        }

        public ImageCanvas(GrayscaleBitmap bmp, GrayscaleDrawMode mode, List<FaceLocation> faces) {
            this.image = new BufferedImage(bmp.getWidth(), bmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < bmp.getWidth(); x++) {
                for (int y = 0; y < bmp.getHeight(); y++) {
                    if (bmp.pixels[x][y] > 1.0 ||
                            (mode == GrayscaleDrawMode.SIMPLE_POSITIVE && bmp.pixels[x][y] < 0.0) ||
                            (mode == GrayscaleDrawMode.EXTENDED_HEIGHTMAP && bmp.pixels[x][y] < -1.0)) {
                        throw new RuntimeException("Invalid pixel value!");
                    }
                    if (mode == GrayscaleDrawMode.SIMPLE_POSITIVE) {
                        final int ipix = (int) Math.round(bmp.pixels[x][y] * 255.0);
                        final int p = 0xFF << 24 |
                                ipix << 16 |
                                ipix << 8 |
                                ipix;
                        image.setRGB(x, y, p);
                    } else {
                        final int green = bmp.pixels[x][y] >= 0
                                ? (int) Math.round(bmp.pixels[x][y] * 255.0) : 0;
                        final int red = 0;
                        final int blue = bmp.pixels[x][y] <= 0
                                ? (int) Math.round(bmp.pixels[x][y] * -255.0) : 0;
                        final int p = 0xFF << 24 |
                                red << 16 |
                                green << 8 |
                                blue;
                        image.setRGB(x, y, p);
                    }
                }
            }
            this.faces = faces;
        }

        public ImageCanvas(RgbBitmap bmp, RgbDrawMode mode) {
            this.image = new BufferedImage(bmp.getWidth(), bmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < bmp.getWidth(); x++) {
                for (int y = 0; y < bmp.getHeight(); y++) {
                    int p = 0xFF << 24;
                    switch (mode) {
                    case R:
                        p |= bmp.pixels[x][y].r << 16;
                        break;
                    case G:
                        p |= bmp.pixels[x][y].g << 8;
                        break;
                    case B:
                        p |= bmp.pixels[x][y].b;
                        break;
                    case I:
                        p |= bmp.pixels[x][y].i << 16;
                        p |= bmp.pixels[x][y].i << 8;
                        p |= bmp.pixels[x][y].i;
                        break;
                    default:
                        p |= bmp.pixels[x][y].r << 16;
                        p |= bmp.pixels[x][y].g << 8;
                        p |= bmp.pixels[x][y].b;
                        break;
                    }
                    image.setRGB(x, y, p);
                }
            }
            this.faces = null;
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(image, 0, 0, null);
            if (faces != null) {
                g.setColor(Color.GREEN);
                for (final FaceLocation fl : faces) {
                    g.drawRect(fl.x, fl.y, fl.w, fl.h);
                }
            }
        }
    }

    /**
     * Draw BufferedImage as it is.
     * @param title
     * @param image
     */
    public ImagePreviewWindow(String title, BufferedImage image) {
        this(title, image.getWidth(), image.getHeight(), new ImageCanvas(image));
    }

    /**
     * Create preview window for a RgbBitmap.
     * @param title
     * @param bmp
     * @param mode
     */
    public ImagePreviewWindow(String title, RgbBitmap bmp, RgbDrawMode mode) {
        this(title, bmp.getWidth(), bmp.getHeight(), new ImageCanvas(bmp, mode));
    }

    /**
     * Create preview window for a GrayscaleBitmap.
     * @param title
     * @param bmp
     */
    public ImagePreviewWindow(String title, GrayscaleBitmap bmp) {
        this(title, bmp.getWidth(), bmp.getHeight(), new ImageCanvas(bmp, GrayscaleDrawMode.SIMPLE_POSITIVE));
    }

    public ImagePreviewWindow(String title, GrayscaleBitmap bmp, GrayscaleDrawMode mode) {
        this(title, bmp.getWidth(), bmp.getHeight(), new ImageCanvas(bmp, mode));
    }

    public ImagePreviewWindow(String title, GrayscaleBitmap bmp, List<FaceLocation> faces) {
        this(title, bmp.getWidth(), bmp.getHeight(), new ImageCanvas(bmp, GrayscaleDrawMode.SIMPLE_POSITIVE, faces));
    }

    /**
     * Universal constructor using pre-created Canvas.
     * @param title
     * @param width
     * @param height
     * @param canvas
     */
    private ImagePreviewWindow(String title, int width, int height, Canvas canvas) {
        setTitle(title);
        setSize(width, height + 30);
        setLocationByPlatform(true);
//        setLocation(getScreenLocation());
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        canvas.setMinimumSize(new Dimension(width, height));
        getContentPane().add(canvas);
    }

    /**
     * Return location for 2nd screen if available.
     * @return
     */
    private Point getScreenLocation() {
        Point p1 = null;
        Point p2 = null;
        for (final GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            if (p1 == null) {
                p1 = gd.getDefaultConfiguration().getBounds().getLocation();
            } else if (p2 == null) {
                p2 = gd.getDefaultConfiguration().getBounds().getLocation();
            }
        }
        if (p2 == null) {
            p2 = p1;
        }
        return p2;
    }

}
