package magic.ui.image.download;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
import magic.ui.widget.FontsAndBorders;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DirectoryChooser extends JPanel {

    static final String CP_FOLDER_CHANGED = "imageFolderUChanged";

    // translatable strings
    private static final String _S1 = "Choose a folder...";
    private static final String _S3 = "Not enough free space!";
    private static final String _S2 = "Select images directory";
    private static final String _S4 = "A complete set of images requires at least 1.5 GB of free space.";

    private static final long MIN_FREE_SPACE = 1610612736; // bytes = 1.5 GB

    private final MigLayout layout = new MigLayout();
    private final JTextField textField = new JTextField();
    private final JButton selectButton = new JButton();
    private File defaultPath;

    DirectoryChooser(final Path defaultPath) {
        this.defaultPath = defaultPath.toFile();
        setupTextField();
        setupSelectButton();
        setLayout(layout);
        refreshLayout();
    }
    
    private void openFolderInFileManager() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            DesktopUtils.openDirectory(textField.getText());
        } catch (IOException | IllegalArgumentException ex) {
            ScreenController.showWarningMessage(ex.getMessage());
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void setImagesPath(final File f) {
        if (!f.equals(defaultPath)) {
            if (MagicFileSystem.directoryContains(MagicFileSystem.INSTALL_PATH, f.toPath())) {
                textField.setText(MagicFileSystem.getDataPath(DataPath.IMAGES).toString());
            } else {
                textField.setText(f.toString());
            }
            firePropertyChange(CP_FOLDER_CHANGED, defaultPath, f);
            defaultPath = f;
        }
    }

    private void setupSelectButton() {
        selectButton.setText("...");
        selectButton.setFont(FontsAndBorders.FONT1);
        selectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new ImagesDirectoryChooser(defaultPath.toString());
                final int action = fileChooser.showOpenDialog(ScreenController.getMainFrame());
                if (action == JFileChooser.APPROVE_OPTION) {
                    setImagesPath(fileChooser.getSelectedFile());
                }
            }
        });
    }

    private void setupTextField() {
        textField.setText(defaultPath.toString());
        textField.setEditable(false);
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                final boolean isDoubleClick = (e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2);
                if (isDoubleClick) {
                    openFolderInFileManager();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

    }

    private void refreshLayout() {
        removeAll();
        layout.setLayoutConstraints("insets 0, gap 0");
        add(textField, "w 100%");
        add(selectButton, "w 26!");
    }

    void addHintSources(HintPanel hintPanel) {
        hintPanel.addHintSource(textField, "<b>Card images folder</b><br>Card images will be downloaded here into the \"cards\" and \"tokens\" sub-folders. Double-click to to open this location in file manager.");
        hintPanel.addHintSource(selectButton, String.format("<b>%s</b><br>%s", UiString.get(_S1), "Select or create a new folder in which card images will be stored."));
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

    Path getPath() {
        return Paths.get(textField.getText());
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        textField.setToolTipText(text);
        selectButton.setToolTipText(text);
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
