package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.about.AboutPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ScreenHeaderPanel extends TexturedPanel
        implements IThemeStyle {

    private class PlaceHolderPanel extends JPanel {
        @Override
        public boolean isVisible() {
            return false;
        }
    }

    public final static int PANEL_HEIGHT = 50;

    private final AboutPanel titlePanel;
    private JComponent contentPanel = new PlaceHolderPanel();
    private JComponent optionsPanel = new PlaceHolderPanel();

    public ScreenHeaderPanel(String title) {

        this.titlePanel = new AboutPanel(title);

        setMinimumSize(new Dimension(getPreferredSize().width, PANEL_HEIGHT));
        refreshStyle();

        setLayout(new MigLayout(
                "insets 0 4 0 8, gap 12, aligny center",
                "[33%, fill][fill, grow][33%, fill]",
                "[fill, grow]")
        );
        setLayout();
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
    }

    public void setContent(JComponent aPanel) {
        this.contentPanel = aPanel;
        setLayout();
    }

    private void setLayout() {
        removeAll();
        add(titlePanel);
        add(contentPanel);
        add(optionsPanel);
        revalidate();
    }

    public void setOptions(JComponent c) {
        this.optionsPanel = c;
        setLayout();
    }

}
