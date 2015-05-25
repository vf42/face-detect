package lv.rtu.dadi.facedetect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.bitmaps.SubWindow;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.haar.HaarLikeFeature;
import lv.rtu.dadi.facedetect.haar.HaarRectangle;

import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * OpenGL Window with ability to visualize scan window and found faces.
 * @author fedorovvadim
 *
 */
public class ImageScanVisualizer extends ImagePreviewWindow {
    private static final long serialVersionUID = 1L;

    private final boolean visualizeFeatures;

    private static class ImageScanCanvas extends GLCanvas implements GLEventListener {

        private static final long serialVersionUID = 1L;

        private final int width;
        private final int height;
        private final GLU glu = new GLU();

        private final File textureFile;
        private Texture texture;

        protected List<FaceLocation> faces = new LinkedList<FaceLocation>();
        private SubWindow window = null;
        private HaarLikeFeature feature = null;

        public ImageScanCanvas(int width, int height, String texturePath) {
            this.width = width;
            this.height = height;
            this.textureFile = new File(texturePath);
            addGLEventListener(this);
        }

        private void loadTexture() {
            try {
                System.out.println("Loading texture...");
                texture = TextureIO.newTexture(textureFile, true);
                System.out.println("Texture estimated memory size = " + texture.getEstimatedMemorySize());
            } catch (final IOException e) {
                e.printStackTrace();
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(bos));
                JOptionPane.showMessageDialog(
                        null,
                        bos.toString(),
                        "Error loading texture",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            final GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

            if (texture != null) {
                texture.enable(gl);
                texture.bind(gl);
                gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                final TextureCoords coords = texture.getImageTexCoords();

                gl.glBegin(GL2.GL_QUADS);
                gl.glTexCoord2f(coords.left(), coords.top());
                gl.glVertex3f(0, 0, 0);
                gl.glTexCoord2f(coords.right(), coords.top());
                gl.glVertex3f(width, 0, 0);
                gl.glTexCoord2f(coords.right(), coords.bottom());
                gl.glVertex3f(width, height, 0);
                gl.glTexCoord2f(coords.left(), coords.bottom());
                gl.glVertex3f(0, height, 0);
                gl.glEnd();
                texture.disable(gl);
            }

            gl.glColor3f(0.0f, 1.0f, 0.0f);
            gl.glLineWidth(1.0f);
            synchronized (faces) {
                for (final FaceLocation fl : faces) {
                    gl.glBegin(GL2.GL_LINE_LOOP);
                    gl.glVertex3f(fl.x, fl.y, 0.1f);
                    gl.glVertex3f(fl.x + fl.w, fl.y, 0.1f);
                    gl.glVertex3f(fl.x + fl.w, fl.y + fl.h, 0.1f);
                    gl.glVertex3f(fl.x, fl.y + fl.h, 0.1f);
                    gl.glEnd();
                }
            }

            if (window != null) {
                gl.glColor3f(1.0f, 0.0f, 0.0f);
                gl.glLineWidth(2.0f);
                gl.glBegin(GL2.GL_LINE_LOOP);
                gl.glVertex3f(window.x, window.y, 0.1f);
                gl.glVertex3f(window.x + window.w, window.y, 0.1f);
                gl.glVertex3f(window.x + window.w, window.y + window.h, 0.1f);
                gl.glVertex3f(window.x, window.y + window.h, 0.1f);
                gl.glEnd();
            }

            if (feature != null) {
                // Get scaling factor.
                final double scale = window.w / feature.width;
                for (final HaarRectangle rect : feature.rects) {
                    final HaarRectangle scRect = rect.scaled(scale);
                    float z = 0.2f;
                    if (scRect.weight > 0) {
                        gl.glColor3f(0.0f, 0.0f, 0.0f);
                        z = 0.3f;
                    } else {
                        gl.glColor3f(1.0f, 1.0f, 1.0f);
                    }
                    gl.glBegin(GL2.GL_POLYGON);
                    gl.glVertex3f(window.x + scRect.x0, window.y + scRect.y0, z);
                    gl.glVertex3f(window.x + scRect.x1, window.y + scRect.y0, z);
                    gl.glVertex3f(window.x + scRect.x1, window.y + scRect.y1, z);
                    gl.glVertex3f(window.x + scRect.x0, window.y + scRect.y1, z);
                    gl.glEnd();
                }
            }
        }

        @Override
        public void dispose(GLAutoDrawable arg0) {

        }

        @Override
        public void init(GLAutoDrawable drawable) {
            final GL2 gl = drawable.getGL().getGL2();
            drawable.setGL(new DebugGL2(gl));

            gl.glClearColor(0, 0, 0, 0);
            gl.glEnable(GL.GL_DEPTH_TEST);

            loadTexture();
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            final GL2 gl = drawable.getGL().getGL2();
            gl.glMatrixMode(GL2ES1.GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluOrtho2D(0, width, height, 0);
            gl.glMatrixMode(GL2ES1.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        public void addFace(FaceLocation face) {
            synchronized (faces) {
                this.faces.add(face);
            }
        }

        public void setWindow(SubWindow sw) {
            this.window = sw;
            repaint();
        }

        public void setFaces(List<FaceLocation> faces) {
            this.faces = faces;
            repaint();
        }

        public void setFeature(HaarLikeFeature feature) {
            this.feature = feature;
            repaint();
        }

    }

    public ImageScanVisualizer(String title, GrayscaleImage bmp, String texturePath, boolean visualizeFeatures) {
        super(title, bmp.getWidth(), bmp.getHeight(),
                new ImageScanCanvas(bmp.getWidth(), bmp.getHeight(), texturePath));
        this.visualizeFeatures = visualizeFeatures;
    }

    public ImageScanVisualizer(String title, GrayscaleImage bmp, String texturePath) {
        this(title, bmp, texturePath, false);
    }

    public void addFace(FaceLocation face) {
        ((ImageScanCanvas) this.canvas).addFace(face);
    }

    public void setWindow(SubWindow sw) {
        ((ImageScanCanvas) this.canvas).setWindow(sw);
    }

    public void setFaces(List<FaceLocation> faces) {
        ((ImageScanCanvas) this.canvas).setFaces(faces);
    }

    public void setFeature(HaarLikeFeature feature) {
        if (visualizeFeatures) {
            ((ImageScanCanvas) this.canvas).setFeature(feature);
        }
    }

}
