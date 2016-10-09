package magic.ui.screen.images.download;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Path;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.FontsAndBorders;
import magic.ui.widget.M.MFileLink;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DirectoryChooser extends JPanel {

    static final String CP_FOLDER_CHANGED = "b896016d-7b11-4295-8b6c-312bec5e04ad";

    // translatable strings
    private static final String _S1 = "Choose a folder...";
    private static final String _S3 = "Not enough free space!";
    private static final String _S2 = "Select images directory";
    private static final String _S4 = "A complete set of images requires at least 1.5 GB of free space.";
    private static final String _S6 = "Magarena will look for a card image in one of the following sub-folders, in the order shown :- 'custom', 'crops', 'cards' or 'tokens'. <b>Left click</b> to explore the images folder.";
    private static final String _S7 = "Select or create a new images folder.";

    private static final long MIN_FREE_SPACE = 1610612736; // bytes = 1.5 GB

    private final MigLayout layout = new MigLayout();
    private final MFileLink imagesFolder = new MFileLink();
    private final JButton selectButton = new JButton();
    private File defaultPath;

    public DirectoryChooser(final Path defaultPath) {
        this.defaultPath = defaultPath.toFile();
        setupImagesFolderField();
        setupSelectButton();
        layout.setLayoutConstraints("insets 0, gap 0");
        setLayout(layout);
        refreshLayout();
    }

    private void setImagesPath(final File f) {
        if (!f.equals(defaultPath)) {
            if (MagicFileSystem.directoryContains(MagicFileSystem.INSTALL_PATH, f.toPath())) {
                imagesFolder.setFile(MagicFileSystem.getDataPath(DataPath.IMAGES));
            } else {
                imagesFolder.setFile(f);
            }
            firePropertyChange(CP_FOLDER_CHANGED, defaultPath, f);
            defaultPath = f;
        }
    }

    private void setupSelectButton() {
        selectButton.setText("...");
        selectButton.setFont(FontsAndBorders.FONT1);
        selectButton.setToolTipText(UiString.get(_S7));
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

    private void setupImagesFolderField() {
        imagesFolder.setFile(defaultPath);
        imagesFolder.setToolTipText(UiString.get(_S6));
        imagesFolder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY), 
                BorderFactory.createEmptyBorder(0, 4, 0, 0))
        );
    }

    private void refreshLayout() {
        add(imagesFolder.component(), "w 100%, h 100%");
        add(selectButton, "w 26!");
    }

    void addHintSources(HintPanel hintPanel) {
        hintPanel.addHintSource(imagesFolder);
        hintPanel.addHintSource(selectButton, String.format("<b>%s</b><br>%s",
            UiString.get(_S1), UiString.get(_S7)
        ));
    }

    private static class ImagesDirectoryChooser extends JFileChooser {
        public ImagesDirectoryChooser(String currentDirectoryPath) {
            super(currentDirectoryPath);
            setDialogTitle(UiString.get(_S2));
            setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            setAcceptAllFileFilterUsed(false);
            // disable the folder name textbox (see #803).
            disableTextFields(this);
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
        private void disableTextFields(Container c) {
            for (Component cmp : c.getComponents()) {
                if (cmp instanceof JTextField) {
                    ((JTextField) cmp).setEnabled(false);
                } else if (cmp instanceof Container) {
                    disableTextFields((Container) cmp);
                }
            }
        }
    }

    public Path getPath() {
        return imagesFolder.getFilePath();
    }

    @Override
    public void setFocusable(boolean b) {
        super.setFocusable(b);
        imagesFolder.setFocusable(b);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (Component c : getComponents()) {
            c.setEnabled(b);
        }
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        imagesFolder.addMouseListener(l);
        selectButton.addMouseListener(l);
    }

}
