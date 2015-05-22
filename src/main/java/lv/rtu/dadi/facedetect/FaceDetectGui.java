package lv.rtu.dadi.facedetect;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;

import lv.rtu.dadi.facedetect.bitmaps.GrayscaleImage;
import lv.rtu.dadi.facedetect.detectors.FaceDetector;
import lv.rtu.dadi.facedetect.detectors.FaceLocation;
import lv.rtu.dadi.facedetect.detectors.violajones.OptimalVJFaceDetector;

import org.apache.commons.imaging.ImageReadException;

/**
 * GUI app for face detection experiments.
 *
 */
public class FaceDetectGui extends JFrame implements ItemListener {
    private static final String DEFAULT_STATUS_STR = "Drag files here";

    private static final long serialVersionUID = 1L;

    private JCheckBox jcbPreviewProcessed;
    private JCheckBox jcbAnimate;
    private JCheckBox jcbAnimateFeatures;
    private JCheckBox jcbShowFinalResult;
    private JButton btnOpenAndDetect;
    private JProgressBar progressBar;

    private final FaceDetector detector;
    private final ReentrantLock detectorLock;

    private String lastDir = null;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    public FaceDetectGui() throws FileNotFoundException, XMLStreamException {
        initUI();
        detector = new OptimalVJFaceDetector();
        detectorLock = new ReentrantLock();
    }

    /**
     * Perform all UI-related configuration.
     */
    private void initUI() {
        initMenu();
        setTitle("Face Detect");
        setSize(250, 200);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setLocationByPlatform(false);
        setLocation(0, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(4, 4));

        final JPanel panel = new JPanel();
        final BoxLayout panelLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(Box.createVerticalGlue());
        panel.setLayout(panelLayout);
        getContentPane().add(panel, BorderLayout.NORTH);

        btnOpenAndDetect = new JButton("Open & Detect");
        btnOpenAndDetect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    openAndDetect();
                } catch (ImageReadException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel.add(btnOpenAndDetect);

        jcbShowFinalResult = new JCheckBox("Show final result");
        jcbShowFinalResult.setSelected(true);
        jcbShowFinalResult.setEnabled(false);
        jcbPreviewProcessed = new JCheckBox("Preview processed");
        jcbAnimate = new JCheckBox("Animate sub-window scan");
        jcbAnimateFeatures = new JCheckBox("Animate Haar-like features");
        jcbAnimateFeatures.setEnabled(false);

        jcbPreviewProcessed.addItemListener(this);
        jcbAnimate.addItemListener(this);
        jcbAnimateFeatures.addItemListener(this);
        jcbPreviewProcessed.addItemListener(this);

        panel.add(jcbPreviewProcessed);
        panel.add(jcbAnimate);
        panel.add(jcbAnimateFeatures);
        panel.add(jcbShowFinalResult);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString(DEFAULT_STATUS_STR);
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        // Setup drag&drop.
        this.setDropTarget(new DropTarget() {
            private static final long serialVersionUID = 1L;

            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    if (!detectorLock.isLocked()) {
                        evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        @SuppressWarnings("unchecked")
                        final
                        List<File> droppedFiles = (List<File>) evt.getTransferable()
                                .getTransferData(DataFlavor.javaFileListFlavor);
                        for (final File file : droppedFiles) {
                            detectInFile(file);
                        }
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initMenu() {
        final JMenuBar menubar = new JMenuBar();

        final JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        menubar.add(file);

        final JMenuItem open = new JMenuItem("Open...");
        open.setMnemonic(KeyEvent.VK_O);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    openAndDetect();
                } catch (ImageReadException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        file.add(open);
        file.addSeparator();

        final JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(event -> System.exit(0));
        file.add(exit);

        setJMenuBar(menubar);
    }

    public void openAndDetect() throws ImageReadException, IOException {
        final File f = openFile();
        if (f == null) {
            return;
        }
        detectInFile(f);
    }

    private void detectInFile(final File f) {
        final JFrame parentWindow = this;
        executor.schedule(new Runnable() {

            @Override
            public void run() {
                try {
                    btnOpenAndDetect.setEnabled(false);
                    detectorLock.lock();
                    progressBar.setString(f.getName());

                    final GrayscaleImage scene = new GrayscaleImage(ImageUtils.readImage(f));
                    int winLocShift = 0;
                    if (jcbPreviewProcessed.isSelected()) {
                        final ImagePreviewWindow ipw =
                                new ImagePreviewWindow(f.getName() + " processed", detector.getPreprocessed(scene));
                        ipw.setLocationRelativeTo(parentWindow);
                        ipw.setLocation(parentWindow.getWidth() + 5, 0);
                        winLocShift += 35;
                    }
                    if (jcbAnimate.isSelected()) {
                        final ImageScanVisualizer visual = new ImageScanVisualizer(
                                f.getName() + " scan", scene, f.getAbsolutePath(),
                                jcbAnimateFeatures.isSelected());
                        visual.setLocationRelativeTo(parentWindow);
                        visual.setLocation(parentWindow.getWidth() + 5 + winLocShift, winLocShift);
                        winLocShift += 35;
                        detector.setVisual(visual);
                        // Wait for it to initialize.
                        try {
                            Thread.sleep(500);
                        } catch (final InterruptedException e) {
                        }
                    } else {
                        detector.setVisual(null);
                    }
                    progressBar.setIndeterminate(true);
                    final List<FaceLocation> faces = detector.detectFaces(scene);
                    if (jcbShowFinalResult.isSelected()) {
                        final ImagePreviewWindow ipw = new ImagePreviewWindow(f.getName() + " result", scene, faces);
                        ipw.setLocationRelativeTo(parentWindow);
                        ipw.setLocation(parentWindow.getWidth() + 5 + winLocShift, winLocShift);
                    }
                } catch (final Exception e1) {
                    e1.printStackTrace();
                } finally {
                    progressBar.setString(DEFAULT_STATUS_STR);
                    detectorLock.unlock();
                    progressBar.setIndeterminate(false);
                    btnOpenAndDetect.setEnabled(true);
                }
            }
        }, 10, TimeUnit.MILLISECONDS);
    }

    private File openFile() {
        final JFileChooser fc = new JFileChooser(lastDir != null ? lastDir : System.getProperty("user.dir"));
        fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp", "jpeg"));
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            final File result = fc.getSelectedFile();
            lastDir = result.getParent();
            return result;
        } else {
            return null;
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        final Object source = e.getItemSelectable();
        if (source == jcbShowFinalResult) {

        } else if (source == jcbPreviewProcessed) {

        } else if (source == jcbAnimate) {
            jcbAnimateFeatures.setEnabled(jcbAnimate.isSelected());
        } else if (source == jcbAnimateFeatures) {

        }

        // If none is selected - select ShowFinalResult.
        if (e.getStateChange() == ItemEvent.DESELECTED
                && !jcbPreviewProcessed.isSelected()
                && !jcbAnimate.isSelected()) {
            jcbShowFinalResult.setSelected(true);
            jcbShowFinalResult.setEnabled(false);
        }
        // If anything else is selected - make ShowFinalResult available.
        if (e.getStateChange() == ItemEvent.SELECTED
                && (jcbPreviewProcessed.isSelected() || jcbAnimate.isSelected())) {
            jcbShowFinalResult.setEnabled(true);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException,
            ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new FaceDetectGui();
    }

}
