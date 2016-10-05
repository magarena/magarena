package magic.ui.screen.menu.migrate;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.ui.ImportWorker;
import magic.ui.MagarenaDirectoryChooser;
import magic.ui.ScreenController;
import magic.ui.URLUtils;
import magic.translate.UiString;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ImportScreen extends AbstractScreen {

    // translatable strings
    private static final String _S1 = "Import Settings?";
    private static final String _S2 = "Yes";
    private static final String _S3 = "Import settings from a previous version of Magarena.";
    private static final String _S4 = "No";
    private static final String _S5 = "Help";
    private static final String _S6 = "Opens the related wiki page in your internet browser.";
    private static final String _S7 = "Importing...";
    private static final String _S8 = "Cancel";

    private final JLabel progressLabel = new JLabel();
    private final StringBuffer sb = new StringBuffer();

    public ImportScreen() {
        setContent(new ScreenContent());
    }

    private class ScreenContent extends JPanel{

        private final MigLayout migLayout = new MigLayout();
        private MenuPanel menuPanel;

        public ScreenContent() {

            setOpaque(false);

            progressLabel.setForeground(Color.WHITE);
            progressLabel.setVerticalAlignment(SwingConstants.TOP);
            progressLabel.setFont(FontsAndBorders.FONT_README);

            setLayout(migLayout);
            migLayout.setLayoutConstraints("alignx center, aligny center");

            showImportMenu();

        }

        private void refreshLayout() {
            removeAll();
            add(menuPanel);
            revalidate();
        }

        private void showImportMenu() {
            menuPanel = new ImportMenuPanel();
            refreshLayout();
        }

        private void showImportProgressPanel(File importFolder) {
            menuPanel = new ImportProgressPanel(importFolder);
            refreshLayout();
        }

        private void doImport() {
            final JFileChooser fileChooser = new MagarenaDirectoryChooser();
            final int action = fileChooser.showOpenDialog(ScreenController.getMainFrame());
            if (action == JFileChooser.APPROVE_OPTION) {
                showImportProgressPanel(fileChooser.getSelectedFile());
            }
        }

        private class ImportMenuPanel extends MenuPanel {

            ImportMenuPanel() {

                super(UiString.get(UiString.get(_S1)));

                addMenuItem(UiString.get(UiString.get(_S2)), new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        doImport();
                    }
                }, UiString.get(_S3));

                addMenuItem(UiString.get(UiString.get(_S4)), new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        ScreenController.showMainMenuScreen();
                    }
                });

                addMenuItem(UiString.get(UiString.get(_S5)), new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        URLUtils.openURL(URLUtils.URL_WIKI + "Upgrading-to-a-new-release");
                    }
                }, UiString.get(_S6));

                refreshLayout();

//            final JLabel lbl = new JLabel("<html>Import settings from previous version?</html>");
//            lbl.setForeground(Color.WHITE);
//            lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
//            menuPanel.add(lbl, "w 100%, gaptop 10, gapbottom 10", 1);
            }

        }

        private class ImportProgressPanel extends MenuPanel implements PropertyChangeListener {

            private final JPanel panel = new JPanel(new MigLayout("insets 4 0 0 0, flowy"));
            private final MenuButton cancelButton;
            private ImportWorker importWorker;
            private final ProgressPanel progressBar = new ProgressPanel();

            public ImportProgressPanel(File aFolder) {

                super(UiString.get(UiString.get(_S7)));

                cancelButton = new MenuButton(
                        UiString.get(_S8),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent ev) {
                                setVisible(false);
                                if (importWorker != null) {
                                    importWorker.cancel(false);
                                } else {
                                    ScreenController.showMainMenuScreen();
                                }
                            }
                        });

                cancelButton.setVisible(true);

                panel.setOpaque(false);
                refreshPanelLayout();

                add(panel, "w 100%, h 100%");

                importWorker = new ImportWorker(aFolder);
                importWorker.addPropertyChangeListener(this);
                importWorker.execute();

            }

            final void refreshPanelLayout() {
                panel.removeAll();
                panel.add(progressLabel, "w 100%, h 100%");
                panel.add(progressBar, "w 100%, h 4!");
                panel.add(cancelButton, "w 100%, h 26!, hidemode 3");
                panel.revalidate();
            }

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                assert SwingUtilities.isEventDispatchThread();

                if (evt.getPropertyName().equalsIgnoreCase("progress")) {
                    progressBar.setValue((int)evt.getNewValue());

                } else if (evt.getPropertyName().equalsIgnoreCase("state")) {
                    if (evt.getNewValue().toString().equalsIgnoreCase("done")) {
                        setVisible(false);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                ThemeFactory.getInstance().loadThemes();
                                ScreenController.showMainMenuScreen();
                            }
                        });
                    }

                } else if (evt.getPropertyName().equalsIgnoreCase("progressNote")) {

                    sb.append(evt.getNewValue().toString().replace("\n", "<br>"));

                    progressLabel.setText("<html>" + sb.toString() + "</html>");
                    progressLabel.revalidate();
                    progressLabel.repaint();

                }
            }

        }

    }

    private class ProgressPanel extends JPanel {

        private int percentageComplete = 0;

        public ProgressPanel() {
            setOpaque(false);
        }

        private void setValue(int progressValue) {
            percentageComplete = progressValue;
            repaint();
        }

        private final Color COLOR1 = new Color(255, 255, 255, 0);
        private final Color COLOR2 = new Color(255, 255, 255, 200);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2d = (Graphics2D) g.create();
            if (percentageComplete > 0) {
                final int w = (int)(getWidth() * (percentageComplete / 100D));
                g2d.setPaint(new GradientPaint(0, 0, COLOR1, w, 0, COLOR2));
                g2d.fillRect(0, 0, w, getHeight());
            }
            g2d.dispose();
        }

    }

}
