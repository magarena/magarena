package magic.ui.screen;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.awt.MagicFont;
import magic.ui.MagicImages;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.throbber.AbstractThrobber;
import magic.ui.widget.throbber.ImageThrobber;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ScreenLoadingPanel extends TexturedPanel {

    private final Runnable runnable;
    private final boolean needsCardData;
    
    private AbstractThrobber throbber;
    private final JLabel lbl = new JLabel();

    ScreenLoadingPanel(Runnable r, boolean cdata) {
        this.needsCardData = cdata;
        this.runnable = r;
        setDefaultProperties();
        setLayout();
    }
    
    void setMessage(String text) {
        lbl.setText("... " + text + " ...");
    }

    private void setLayout() {
        setLayout(new MigLayout("flowy, ay center", "[fill, grow, center]"));
        add(throbber);
        add(lbl);
    }

    private void setDefaultProperties() {

        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG.darker());

        throbber = new ImageThrobber.Builder(
                MagicImages.loadImage("round-shield.png")).build();
        
        lbl.setFont(MagicFont.BelerenBold.get().deriveFont(22f));
        lbl.setForeground(Color.WHITE);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setText("..."); // need to set some default text for smooth layout.
    }

    Runnable getRunnable() {
        return runnable;
    }

    boolean isCardDataNeeded() {
        return needsCardData;
    }
}
