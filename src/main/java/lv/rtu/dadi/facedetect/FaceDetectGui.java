package lv.rtu.dadi.facedetect;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

/**
 * GUI app for face detection experiments.
 *
 */
public class FaceDetectGui extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private Canvas canvas;

    public FaceDetectGui() {
        initUI();
    }

    /**
     * Perform all UI-related configuration.
     */
    private void initUI() {
        initMenu();

        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        final JButton btnRun = new JButton("Run");
        btnRun.addActionListener(e -> doRun());
        btnRun.setFont(new Font("Tahoma", Font.BOLD, 11));
        toolBar.add(btnRun);

        final JSplitPane splitPane = new JSplitPane();
        getContentPane().add(splitPane, BorderLayout.CENTER);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMinimumSize(new Dimension(150, 100));
        splitPane.setLeftComponent(scrollPane);

        final JPanel panel = new JPanel();
        splitPane.setRightComponent(panel);

                        canvas = new Canvas();
                        panel.add(canvas);
                        panel.setMinimumSize(new Dimension(300, 300));
                        canvas.setBackground(new Color(102, 204, 255));
                        canvas.setMinimumSize(new Dimension(300, 300));
        panel.getGraphics().drawOval(50, 50, 20, 20);
    }

    private void initMenu() {
        final JMenuBar menubar = new JMenuBar();

        final JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        menubar.add(file);

        final JMenuItem open = new JMenuItem("Open...");
        open.setMnemonic(KeyEvent.VK_O);
        file.add(open);
        file.addSeparator();

        final JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(event -> System.exit(0));
        file.add(exit);

        final JMenu algorithm = new JMenu("Algorithm");
        file.setMnemonic(KeyEvent.VK_A);
        menubar.add(algorithm);

        final JRadioButtonMenuItem rdbtnmntmDummy = new JRadioButtonMenuItem("Dummy");
        rdbtnmntmDummy.setSelected(true);
        buttonGroup.add(rdbtnmntmDummy);
        algorithm.add(rdbtnmntmDummy);

        setJMenuBar(menubar);
    }

    public static void main(String[] args) {
        new FaceDetectGui();
    }

    /**
     * Run the selected algorithm.
     */
    private void doRun() {
        canvas.setForeground(Color.red);
        canvas.getGraphics().drawRect(10, 10, 100, 100);
    }
}
