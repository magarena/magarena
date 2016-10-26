package magic.ui.widget.M;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import magic.exception.DesktopNotSupportedException;
import magic.translate.MText;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.helpers.DesktopHelper;
import magic.ui.helpers.MouseHelper;

/**
 * Given a file, displays the full path on which a left or right
 * mouse-click will open the file or the containing folder respectively.
 */
public class MFileLink extends MWidget {

    // translatable strings
    private static final String _S1 = "<b>Left click</b> to open file.";
    private static final String _S2 = "<b>Right click</b> to explore containing directory.";
    private static final String _S3 = "<b>Left click</b> to explore directory.";
    private static final String _S4 = "Unable to open file :\n%s\n\n%s";

    private final JLabel label = new JLabel();
    private File file;

    public MFileLink() {
        setMouseClickActions();
    }

    private void openFile() {
        try {
            DesktopHelper.openFileInDefaultOsEditor(file);
        } catch (IOException | DesktopNotSupportedException ex) {
            ScreenController.showWarningMessage(MText.get(_S4, file, ex.getMessage()));
        }
    }

    private void doExploreContainingDirectory() {
        try {
            DesktopHelper.openContainingDirectory(file);
        } catch (IOException ex) {
            ScreenController.showWarningMessage(MText.get(_S4, file, ex.getMessage()));
        }
    }

    private void doExploreDirectory() {
        try {
            DesktopHelper.openDirectory(file);
        } catch (IOException ex) {
            ScreenController.showWarningMessage(MText.get(_S4, file, ex.getMessage()));
        }
    }

    private void doLeftClickAction() {
        if (file.isFile()) {
            openFile();
        } else if (file.isDirectory()) {
            doExploreDirectory();
        }
    }

    private void doRightClickAction() {
        if (file.isFile()) {
            doExploreContainingDirectory();
        } else {
            MagicSound.BEEP.play();
        }
    }

    private void setMouseClickActions() {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MouseHelper.showBusyCursor(label);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    doLeftClickAction();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    doRightClickAction();
                }
                MouseHelper.showHandCursor(label);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                MouseHelper.showHandCursor(label);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                MouseHelper.showDefaultCursor(label);
            }
        });
    }

    @Override
    public final JComponent component() {
        return label;
    }

    private void setToolTip() {
        if (file.isFile()) {
            label.setToolTipText(String.format("<html>%s<br>%s</html>",
                    MText.get(_S1), MText.get(_S2))
            );
        } else if (file.isDirectory()) {
            label.setToolTipText(String.format("<html>%s</html>", MText.get(_S3)));
        }
    }

    public void setFile(final File aFile) {
        file = aFile;
        label.setText(aFile.getAbsolutePath());
        setToolTip();
    }

    public void setFile(final Path aPath) {
        setFile(aPath.toFile());
    }

    public Path getFilePath() {
        return file.toPath();
    }

    //
    // swing component delegates
    //
    public void setOpaque(boolean b) {
        label.setOpaque(b);
    }

    public void setBackground(Color aColor) {
        label.setBackground(aColor);
    }

    public void setForeground(Color aColor) {
        label.setForeground(aColor);
    }

    public void setFont(Font aFont) {
        label.setFont(aFont);
    }

    public void setVisible(boolean b) {
        label.setVisible(b);
    }

    public void setFocusable(boolean b) {
        label.setFocusable(b);
    }

    public void setBorder(Border aBorder) {
        label.setBorder(aBorder);
    }
}
