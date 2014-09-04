package magic.ui.widget;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import magic.MagicMain;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DirectoryChooser extends JPanel implements MouseListener {

    private static final long MIN_FREE_SPACE = 1610612736; // bytes = 1.5 GB

    private final MigLayout layout = new MigLayout();
    private final JTextField textField = new JTextField();
    private final JButton selectButton = new JButton();
    private final Path defaultPath;

    public DirectoryChooser(final Path defaultPath) {

        this.defaultPath = defaultPath;

        setLookAndFeel();
        refreshLayout();

        selectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new ImagesDirectoryChooser(defaultPath.toString());
                final int action = fileChooser.showOpenDialog(MagicMain.rootFrame);
                if (action==JFileChooser.APPROVE_OPTION) {
                    try {
                        textField.setText(fileChooser.getSelectedFile().getCanonicalPath());
                        System.out.println(fileChooser.getSelectedFile().getFreeSpace());
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    private void setLookAndFeel() {
        setLayout(layout);
        // JTextField
        textField.setText(defaultPath.toString());
        textField.setEditable(false);
        textField.addMouseListener(this);
        // JButton
        selectButton.setText("...");
        selectButton.setFont(FontsAndBorders.FONT1);
        selectButton.setToolTipText("Choose directory...");
        selectButton.addMouseListener(this);
    }

    private void refreshLayout() {
        removeAll();
        layout.setLayoutConstraints("insets 0, gap 0");
        add(textField, "w 100%");
        add(selectButton, "w 26!");
    }

    private static class ImagesDirectoryChooser extends JFileChooser {
        public ImagesDirectoryChooser(String currentDirectoryPath) {
            super(currentDirectoryPath);
            setDialogTitle("Select images directory");
            setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            setAcceptAllFileFilterUsed(false);
        }
        @Override
        public void approveSelection() {
            final Path directoryPath = getSelectedFile().toPath();
            if (directoryPath.toFile().getFreeSpace() > MIN_FREE_SPACE) {
                super.approveSelection();
            } else {
                JOptionPane.showMessageDialog(
                        MagicMain.rootFrame,
                        "<html><b>Not enough free space!</b><br>A complete set of images requires at least 1.5 GB of free space.",
                        "Invalid directory",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public Path getPath() {
        return Paths.get(textField.getText());
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        textField.setToolTipText(text);
    }

    /*
     * MouseListener
     */
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == textField && e.getButton() == MouseEvent.BUTTON3) {
            try {
                MagicFileSystem.openDirectory(textField.getText());
            } catch (IOException | IllegalArgumentException  e1) {
                JOptionPane.showMessageDialog(this.getParent().getParent(), e1.getMessage(), "Failed to open File Explorer", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        dispatchEvent(e);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        dispatchEvent(e);
    }

    public boolean isValidDirectory() {
        try {
            return getPath().toFile().isDirectory();
        } catch (InvalidPathException e) {
            return false;
        }
    }

}
