package magic.ui.widget;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.utility.DesktopUtils;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DirectoryChooser extends JPanel implements MouseListener {

    public static final String CP_FOLDER_CHANGED = "imageFolderUChanged";

    // translatable strings
    private static final String _S1 = "Choose directory...";
    private static final String _S3 = "Not enough free space!";
    private static final String _S2 = "Select images directory";
    private static final String _S4 = "A complete set of images requires at least 1.5 GB of free space.";

    private static final long MIN_FREE_SPACE = 1610612736; // bytes = 1.5 GB

    private final MigLayout layout = new MigLayout();
    private final JTextField textField = new JTextField();
    private final JButton selectButton = new JButton();
    private Path defaultPath;

    public DirectoryChooser(final Path defaultPath) {

        this.defaultPath = defaultPath;

        setLookAndFeel();
        refreshLayout();

        selectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new ImagesDirectoryChooser(getDefaultPath().toString());
                final int action = fileChooser.showOpenDialog(ScreenController.getMainFrame());
                if (action == JFileChooser.APPROVE_OPTION) {
                    setImagesPath(fileChooser.getSelectedFile());
                }
            }
        });
    }

    private File getDefaultPath() {
        return defaultPath.toFile();
    }

    private void setImagesPath(final File f) {
        if (!f.equals(getDefaultPath())) {
            if (MagicFileSystem.directoryContains(MagicFileSystem.INSTALL_PATH, f.toPath())) {
                textField.setText(MagicFileSystem.getDataPath(DataPath.IMAGES).toString());
            } else {
                textField.setText(f.toString());
            }
            firePropertyChange(CP_FOLDER_CHANGED, defaultPath.toFile(), f);
            defaultPath = f.toPath();
        }
    }

    private void setLookAndFeel() {
        setLayout(layout);
        // JTextField
        textField.setText(defaultPath.toString());
        textField.setEditable(false);
        textField.addMouseListener(this);
        textField.setToolTipText("<html><b>Images folder</b><br>Double-click to open in the default file manager.");
        // JButton
        selectButton.setText("...");
        selectButton.setFont(FontsAndBorders.FONT1);
        selectButton.setToolTipText(UiString.get(_S1));
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
            setDialogTitle(UiString.get(_S2));
            setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            setAcceptAllFileFilterUsed(false);
        }
        @Override
        public void approveSelection() {
            final Path directoryPath = getSelectedFile().toPath();
            if (directoryPath.toFile().getFreeSpace() > MIN_FREE_SPACE) {
                super.approveSelection();
            } else {
                ScreenController.showWarningMessage(
                        String.format("<html><b>%s</b><br>%s<html>",
                                UiString.get(_S3),
                                UiString.get(_S4))
                );
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
        if (e.getSource() == textField && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                DesktopUtils.openDirectory(textField.getText());
            } catch (IOException | IllegalArgumentException e1) {
                ScreenController.showWarningMessage(e1.getMessage());
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dispatchEvent(e);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        dispatchEvent(e);
    }

    public boolean isValidDirectory() {
        try {
            return getPath().toFile().isDirectory();
        } catch (InvalidPathException e) {
            return false;
        }
    }

    @Override
    public void setFocusable(boolean b) {
        super.setFocusable(b);
        textField.setFocusable(b);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (Component c : getComponents()) {
            c.setEnabled(b);
        }
    }

}
