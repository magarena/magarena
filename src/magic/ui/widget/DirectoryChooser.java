package magic.ui.widget;

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

    // translatable strings
    private static final String _S1 = "Choose directory...";
    private static final String _S3 = "Not enough free space!";
    private static final String _S2 = "Select images directory";
    private static final String _S4 = "A complete set of images requires at least 1.5 GB of free space.";

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
                final int action = fileChooser.showOpenDialog(ScreenController.getMainFrame());
                if (action == JFileChooser.APPROVE_OPTION) {
                    final File f = fileChooser.getSelectedFile();
                    if (MagicFileSystem.directoryContains(MagicFileSystem.INSTALL_PATH, f.toPath())) {
                        textField.setText(MagicFileSystem.getDataPath(DataPath.IMAGES).toString());
                    } else {
                        textField.setText(f.toString());
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
        if (e.getSource() == textField && e.getButton() == MouseEvent.BUTTON3) {
            try {
                DesktopUtils.openDirectory(textField.getText());
            } catch (IOException | IllegalArgumentException  e1) {
                ScreenController.showWarningMessage(e1.getMessage());
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
