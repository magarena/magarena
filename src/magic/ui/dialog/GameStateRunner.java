package magic.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import magic.test.TestGameBuilder;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.dialog.button.CancelButton;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GameStateRunner
    extends MagicDialog
    implements ActionListener {

    private JList<String> testClasses = new JList<>();

    public GameStateRunner() {

        super(MText.get("Test Classes (double-click to select)"), new Dimension(500, 460));

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
        for (final String c : MText.getClassNamesInPackage(MagicSystem.getJarFile(), "magic.test")) {
            final String longClassName = c.substring(0, c.length() - ".class".length());
            final String shortClassName = longClassName.substring(longClassName.lastIndexOf(".") + 1);
            final boolean isValid = shortClassName.startsWith("Test")
                && !"TestGameBuilder".equals(shortClassName)
                && !"TestGameBuilder$1".equals(shortClassName);
            if (isValid) {
                classes.add(shortClassName);
            }
        }
        return classes.toArray(new String[classes.size()]);
    }

    private void loadTestClassAndRun(String testClassName) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ScreenController.showDuelGameScreen(TestGameBuilder.buildGame(testClassName));
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
                    loadTestClassAndRun(testClasses.getSelectedValue());
                }
            }
        });

        final JScrollPane listScroller = new JScrollPane(testClasses);
        listScroller.setPreferredSize(new Dimension(getWidth(), getHeight()));

        final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0"));
        panel.add(listScroller, "w 100%, h 100%");
        return panel;
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
        // do nothing.
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
