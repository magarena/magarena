package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ScreenHeaderPanel extends TexturedPanel
        implements IThemeStyle {

    private static final JPanel TEMP_PANEL = new JPanel() {
        @Override
        public boolean isVisible() {
            return false;
        }
    };

    // translatable strings
    private static final String _S1 = "Wiki Help [F1]";
    private static final String _S2 = "Opens the wiki help page for this screen in your browser.";
    private static final String _S3 = "Options Menu [ESC]";
    private static final String _S4 = "Displays menu of common and screen specific options.";

    public final static int PANEL_HEIGHT = 50;

    private final CaptionPanel titlePanel;
    private JPanel contentPanel = TEMP_PANEL;

    public ScreenHeaderPanel(String title) {

        this.titlePanel = new CaptionPanel(title);

        setMinimumSize(new Dimension(getPreferredSize().width, PANEL_HEIGHT));
        refreshStyle();

        setLayout(new MigLayout(
                "insets 0 4 0 0, gap 12, aligny center",
                "[33%, fill][fill, grow][33%, fill]",
                "[fill, grow]")
        );
        refreshLayout();
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
    }

    public void setContent(JPanel aPanel) {
        this.contentPanel = aPanel;
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        add(titlePanel);
        add(contentPanel);
        add(TEMP_PANEL);
        revalidate();
    }

//    private class OptionsPanel extends JPanel {
//
//        private final MigLayout migLayout = new MigLayout();
//        private JButton wikiButton;
//        private JButton optionsButton;
//
//        public OptionsPanel() {
//            setLookAndFeel();
//            if (screen != null) {
//                setupButtons();
//                refreshLayout();
//            }
//        }
//
//        private void setupButtons() {
//            // wiki button
//            if (screen.hasWikiPage()) {
//                wikiButton = new ActionBarButton(
//                        MagicImages.getIcon(MagicIcon.HELP_ICON),
//                        UiString.get(_S1),
//                        UiString.get(_S2),
//                        new AbstractAction() {
//                            @Override
//                            public void actionPerformed(final ActionEvent e) {
//                                screen.showWikiHelpPage();
//                            }
//                        }
//                );
//            }
//            // options button
//            if (screen.hasOptionsMenu()) {
//                optionsButton = new ActionBarButton(
//                        MagicImages.getIcon(MagicIcon.OPTIONS_ICON),
//                        UiString.get(_S3),
//                        UiString.get(_S4),
//                        new AbstractAction() {
//                            @Override
//                            public void actionPerformed(final ActionEvent e) {
//                                ((IOptionsMenu)screen).showOptionsMenuOverlay();
//                            }
//                        }
//                );
//            }
//        }
//
//        private void refreshLayout() {
//            removeAll();
//            migLayout.setLayoutConstraints("insets 0, gapx 0, alignx right");
//            if (screen.hasOptionsMenu()) {
//                add(optionsButton);
//            }
//            if (screen.hasWikiPage()) {
//                add(wikiButton);
//            }
//        }
//
//        private void setLookAndFeel() {
//            setOpaque(false);
//            setLayout(migLayout);
//        }
//
//    }

}
