package magic.ui.screen;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import magic.translate.UiString;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.utility.FileIO;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class TextFileReaderScreen extends AbstractScreen implements IActionBar {

    // translatable strings
    private static final String _S1 = "Close";

    private Path textFilePath;
    private final ScreenContent screenContent = new ScreenContent();

    /**
     * A subclass can use this method to manipulate the file contents before they are displayed.
     */
    protected abstract String reprocessFileContents(final String fileContent);

    public TextFileReaderScreen() {
        setContent(screenContent);
    }

    protected void setTextFile(final Path textFilePath) {
        this.textFilePath = textFilePath;
        screenContent.setContent(textFilePath);
    }

    private class ScreenContent extends TexturedPanel {

        private final MigLayout migLayout = new MigLayout();
        private final JTextArea textArea = new JTextArea();
        private final JScrollPane scrollPane = new JScrollPane(textArea);

        public ScreenContent() {
            setLookAndFeel();
            refreshLayout();
        }

        private void setLookAndFeel() {
            setLayout(migLayout);
            setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
            // file contents text area
            textArea.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
            textArea.setEditable(false);
            textArea.setFont(FontsAndBorders.FONT_README);
            // scroll pane
            scrollPane.setBorder(FontsAndBorders.BLACK_BORDER);
            scrollPane.setOpaque(false);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(50);
            scrollPane.getVerticalScrollBar().setBlockIncrement(50);
            scrollPane.getViewport().setOpaque(false);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, gap 0, center");
            add(scrollPane, "w 100%, h 100%");
        }

        public void setContent(final Path textFilePath) {
            textArea.setText(getFileContents());
            refreshLayout();
            resetVerticalScrollbar();
        }

        private String getFileContents() {
            try {
                final String fileContents = FileIO.toStr(new File(textFilePath.toString()));
                return reprocessFileContents(fileContents);
            } catch (final java.io.IOException ex) {
                return textFilePath.toAbsolutePath().toString() + "\n" + ex.toString();
            }
        }

        private void resetVerticalScrollbar() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    scrollPane.getVerticalScrollBar().setValue(0);
                }
            });
        }

    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get(_S1));
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

}
