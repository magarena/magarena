package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.IconImages;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StatusBar extends TexturedPanel {

    public final static int PANEL_HEIGHT = 50;

    private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
    private final Color refBG = THEME.getColor(Theme.COLOR_TITLE_BACKGROUND);
    private final Color thisBG = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 220);

    private final AbstractScreen magScreen;

    public StatusBar(final AbstractScreen screen0) {
        this.magScreen = screen0;
        setMinimumSize(new Dimension(getPreferredSize().width, PANEL_HEIGHT));
        setBackground(thisBG);
        layoutMagStatusBar();
    }

    private void layoutMagStatusBar() {
        removeAll();
        setLayout(new MigLayout("insets 0 0 0 16, gap 12, aligny center", "[33%][][33%]"));
        if (magScreen != null) {
            final IStatusBar screen = (IStatusBar)magScreen;
            add(new CaptionPanel(screen.getScreenCaption()));
            add(screen.getStatusPanel() != null ? screen.getStatusPanel() : new JLabel(), "pushx, alignx center");
            add(new OptionsPanel(), "w 100%");
        }
        revalidate();
    }

    private class OptionsPanel extends JPanel {

        private final MigLayout migLayout = new MigLayout();
        private final JButton wikiButton = new JButton();
        private final JButton optionsButton = new JButton();

        public OptionsPanel() {
            setLookAndFeel();
            setButtonActions();
            refreshLayout();
        }

        private void setButtonActions() {
            // wiki button
            if (magScreen.hasWikiPage()) {
                wikiButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        magScreen.showWikiHelpPage();
                    }
                });
            }
            // options button
            if (magScreen.hasOptionsMenu()) {
                optionsButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((IOptionsMenu)magScreen).showOptionsMenuOverlay();
                    }
                });
            }
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, gapx 20, alignx right");
            if (magScreen.hasOptionsMenu()) { add(optionsButton); }
            if (magScreen.hasWikiPage())  { add(wikiButton); }
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
            // wiki button
            wikiButton.setIcon(IconImages.HELP_ICON);
            wikiButton.setHorizontalAlignment(SwingConstants.RIGHT);
            wikiButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            wikiButton.setToolTipText("<html><b>Wiki Help [F1]</b><br>Opens the wiki help page for this screen in your browser.");
            setButtonTransparent(wikiButton);
            // options button
            optionsButton.setIcon(IconImages.OPTIONS_ICON);
            optionsButton.setHorizontalAlignment(SwingConstants.RIGHT);
            optionsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            optionsButton.setToolTipText("<html><b>Options Menu [ESC]</b><br>Displays menu of common and screen sepcific options.");
            setButtonTransparent(optionsButton);

        }

        private void setButtonTransparent(final JButton btn) {
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setBorder(null);
        }
        
    }

}
