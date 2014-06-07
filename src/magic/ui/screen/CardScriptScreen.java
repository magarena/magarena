package magic.ui.screen;

import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.TextFileReaderPanel;
import magic.utility.MagicFiles;
import magic.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

@SuppressWarnings("serial")
public class CardScriptScreen extends AbstractScreen implements IStatusBar, IActionBar {

    public CardScriptScreen(final MagicCardDefinition card) {
        setContent(new ScreenContent(card));
    }

    @Override
    public String getScreenCaption() {
        return "Card Script";
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.AbstractScreen#isScreenReadyToClose(magic.ui.screen.AbstractScreen)
     */
    @Override
    public boolean isScreenReadyToClose(AbstractScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Close");
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

    private class ScreenContent extends JPanel {

        private final MigLayout migLayout = new MigLayout();
        private final ScriptFileViewer scriptViewer;
        private final ScriptFileViewer groovyViewer;
        private final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        public ScreenContent(final MagicCardDefinition card) {

            // script file(s) can be in one of two directories.
            final String scriptsPath = card.isMissing() ? MagicMain.getScriptsMissingPath() : MagicMain.getScriptsPath();
            // script file
            final File scriptFile = new File(scriptsPath, CardDefinitions.getScriptFilename(card));
            scriptViewer = new ScriptFileViewer(scriptFile);
            // groovy file [optional]
            final File groovyFile = new File(scriptsPath, CardDefinitions.getGroovyFilename(card));
            groovyViewer = groovyFile.exists() ? new ScriptFileViewer(groovyFile) : null;

            setLookAndFeel();
            setLayout();

        }

        private void setLayout() {
            removeAll();
            migLayout.setLayoutConstraints("flowy, insets 0");
            if (groovyViewer != null) {
                add(splitter, "w 100%, h 100%");
            } else {
                add(scriptViewer, "w 100%, h 100%");
            }
        }

        private void setLookAndFeel() {
            setLayout(migLayout);
            setOpaque(false);
            if (groovyViewer != null) {
                splitter.setOneTouchExpandable(true);
                splitter.setLeftComponent(scriptViewer);
                splitter.setRightComponent(groovyViewer);
                splitter.setResizeWeight(0.5);
                splitter.setDividerSize(14);
                splitter.setBorder(null);
                splitter.setOpaque(false);
            }
        }
    }

    private class ScriptFileViewer extends JPanel {

        private final MigLayout migLayout = new MigLayout();
        private final ScriptFileViewerHeader headerPanel = new ScriptFileViewerHeader();
        private final TextFileReaderPanel contentsPanel = new TextFileReaderPanel();
        private final File textFile;

        public ScriptFileViewer(final File textFile) {
            this.textFile = textFile;
            setLookAndFeel();
            setLayout();
            setContent();
        }

        private void setContent() {
            headerPanel.setContent(textFile);
            contentsPanel.setContent(textFile.toPath());
        }

        private void setLookAndFeel() {
            setLayout(migLayout);
            setOpaque(false);
        }

        private void setLayout() {
            removeAll();
            migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
            add(headerPanel, "w 100%, h 34!");
            add(contentsPanel, "w 100%, h 100%");
        }

    }

    private class ScriptFileViewerHeader extends JPanel {

        private final Color DEFAULT_FORECOLOR = Color.WHITE;

        private final MigLayout migLayout = new MigLayout();
        private final JLabel headerLabel = new JLabel();
        private File textFile;

        public ScriptFileViewerHeader() {
            setLookAndFeel();
            setLayout();
        }

        private void setLookAndFeel() {
            setLayout(migLayout);
            setOpaque(true);
            setBackground(Color.DARK_GRAY);
            // header label
            headerLabel.setForeground(DEFAULT_FORECOLOR);
            headerLabel.setFont(new Font("dialog", Font.PLAIN, 14));
            headerLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MagicFiles.openFileInDefaultOsEditor(textFile);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    headerLabel.setForeground(MagicStyle.HIGHLIGHT_COLOR);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    headerLabel.setForeground(DEFAULT_FORECOLOR);
                }
            });
        }

        private void setLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0 4 0 4, aligny center");
            add(headerLabel, "w 100%, h 100%");
        }

        public void setContent(final File textFile) {
            this.textFile = textFile;
            headerLabel.setText(textFile.getAbsolutePath());
        }

    }

}
