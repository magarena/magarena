package magic.ui.screen;

import java.awt.Color;
import java.awt.event.HierarchyEvent;
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
class CardsLoadingPanel extends TexturedPanel {

    private final Runnable runnable;
    private CardsLoadingWorker loadingWorker;

    private AbstractThrobber throbber;
    private final JLabel lbl = new JLabel();
    private final MScreen screen;

    CardsLoadingPanel(Runnable r, MScreen screen) {
        this.screen = screen;
        this.runnable = r;
        setDefaultProperties();
        setLayout();
        addHierarchyListener((HierarchyEvent e) -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
                onShowingChanged();
            }
        });
    }

    void onShowingChanged() {
        if (loadingWorker != null && !loadingWorker.isDone()) {
            loadingWorker.cancel(true);
        }
        if (isShowing()) {
            loadingWorker = new CardsLoadingWorker(this);
            loadingWorker.execute();
        }
    }

    void setMessage(String text) {
        lbl.setText(text);
    }

    private void setLayout() {
        setLayout(new MigLayout("flowy, ax center, ay center", "[center]"));
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

    boolean needsPlayableCards() {
        return screen.needsPlayableCards();
    }

    boolean needsMissingCards() {
        return screen.needsMissingCards();
    }
}
