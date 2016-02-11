package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.translate.UiString;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StatusBar extends TexturedPanel implements IThemeStyle {

    // translatable strings
    private static final String _S1 = "Wiki Help [F1]";
    private static final String _S2 = "Opens the wiki help page for this screen in your browser.";
    private static final String _S3 = "Options Menu [ESC]";
    private static final String _S4 = "Displays menu of common and screen specific options.";

    public final static int PANEL_HEIGHT = 50;

    private final AbstractScreen magScreen;

    public StatusBar(final AbstractScreen screen0) {
        this.magScreen = screen0;
        setMinimumSize(new Dimension(getPreferredSize().width, PANEL_HEIGHT));
        refreshStyle();
        layoutMagStatusBar();
    }

    private void layoutMagStatusBar() {
        removeAll();
        setLayout(new MigLayout("insets 0 4 0 0, gap 12, aligny center", "[33%][][33%]"));
        if (magScreen != null) {
            final IStatusBar screen = (IStatusBar)magScreen;
            add(new CaptionPanel(screen.getScreenCaption()));
            add(screen.getStatusPanel() != null ? screen.getStatusPanel() : new JLabel(), "pushx, alignx center");
            add(new OptionsPanel(), "w 100%");
        }
        revalidate();
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
    }

    private class OptionsPanel extends JPanel {

        private final MigLayout migLayout = new MigLayout();
        private JButton wikiButton;
        private JButton optionsButton;

        public OptionsPanel() {
            setupButtons();
            setLookAndFeel();
            refreshLayout();
        }

        private void setupButtons() {
            // wiki button
            if (magScreen.hasWikiPage()) {
                wikiButton = new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.HELP_ICON),
                        UiString.get(_S1),
                        UiString.get(_S2),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                magScreen.showWikiHelpPage();
                            }
                        }
                );
            }
            // options button
            if (magScreen.hasOptionsMenu()) {
                optionsButton = new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.OPTIONS_ICON),
                        UiString.get(_S3),
                        UiString.get(_S4),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                ((IOptionsMenu)magScreen).showOptionsMenuOverlay();
                            }
                        }
                );
            }
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, gapx 0, alignx right");
            if (magScreen.hasOptionsMenu()) {
                add(optionsButton);
            }
            if (magScreen.hasWikiPage()) {
                add(wikiButton);
            }
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
        }

    }

}
