package magic.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import magic.game.state.GameLoader;
import magic.test.TestGameBuilder;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.dialog.button.CancelButton;
import magic.utility.MagicFileSystem;
import static magic.utility.MagicFileSystem.getDataPath;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public class GameStateRunner
    extends MagicDialog
    implements ActionListener {

    // translatable strings
    private static final String _S1 = "Select Game State";

    private JList<String> testClasses = new JList<>();

    public GameStateRunner(final MagicFrame frame) {
        
        super(ScreenController.getMainFrame(), UiString.get(_S1), new Dimension(500, 460));

        setLookAndFeel();
        refreshLayout();

        this.setVisible(true);
    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gap 0, insets 2 6 6 6"));
        panel.add(getMainPanel(), "w 100%, h 100%");
        panel.add(getButtonPanel(), "w 100%, h 30!, pushy, aligny bottom");
    }

    private void setLookAndFeel() {

    }

    private String[] getListOfTestClasses() throws IOException, URISyntaxException {
        final List<String> classes = new ArrayList<>();
        for (final String c : UiString.getClassNamesInPackage(MagicSystem.getJarFile(), "magic.test")) {
            final String longClassName = c.substring(0, c.length() - ".class".length());
            final String shortClassName = longClassName.substring(longClassName.lastIndexOf(".") + 1);
            final boolean isValid = shortClassName.startsWith("Test")
                && !shortClassName.equals("TestGameBuilder")
                && !shortClassName.equals("TestGameBuilder$1");
            if (isValid) {
                classes.add(shortClassName);
            }
        }
        return classes.toArray(new String[classes.size()]);
    }

    private void LoadTestClassAndRun(String testClassName) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ScreenController.showDuelGameScreen(TestGameBuilder.buildGame(testClassName));
        doCancelAndClose();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void loadSaveGameAndRun(String savedGame) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ScreenController.showDuelGameScreen(GameLoader.loadSavedGame(savedGame + ".game"));
        doCancelAndClose();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private JPanel getMainPanel() {
        
        try {
            testClasses = new JList<>(getListOfTestClasses());
        } catch (IOException | URISyntaxException ex) {
            System.err.println(ex);
        }

        testClasses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        testClasses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    LoadTestClassAndRun(testClasses.getSelectedValue());
                }
            }
        });

        final JScrollPane listScroller = new JScrollPane(testClasses);
        listScroller.setPreferredSize(new Dimension(getWidth(), getHeight()));
        
        final JList<String> saves = new JList<>(getSaves());
        saves.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        saves.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    loadSaveGameAndRun(saves.getSelectedValue());
                }
            }
        });
        final JScrollPane listScroller2 = new JScrollPane(saves);
        listScroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, wrap 2"));
        panel.add(new JLabel("Test Class"), "w 100%");
        panel.add(new JLabel("Saved Game"), "w 100%");
        panel.add(listScroller, "w 100%, h 100%");
        panel.add(listScroller2, "w 100%, h 100%");
        return panel;
    }

    private String[] getSaves() {
        final List<String> filenames = new ArrayList<>();
        final Path langPath = getDataPath(MagicFileSystem.DataPath.SAVED_GAMES);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(langPath, "*.game")) {
            for (Path p : ds) {
                filenames.add(FilenameUtils.getBaseName(p.getFileName().toString()));
            }
//            Collections.sort(filenames);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return filenames.toArray(new String[filenames.size()]);
    }

    private JButton getCancelButton() {
        final JButton btn = new CancelButton();
        btn.setFocusable(false);
        btn.addActionListener(getCancelAction());
        return btn;
    }

    private JPanel getButtonPanel() {
        final JPanel panel = new JPanel(new MigLayout("insets 0, alignx right, aligny bottom"));
        panel.add(getCancelButton());
        return panel;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {

    }

    private void doCancelAndClose() {
        dispose();
    }

    @Override
    protected AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancelAndClose();
            }
        };
    }

}
