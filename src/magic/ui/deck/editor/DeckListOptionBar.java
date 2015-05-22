package magic.ui.deck.editor;

import java.awt.Color;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckListOptionBar extends TexturedPanel {

    private final MigLayout miglayout = new MigLayout();

    public DeckListOptionBar() {
        setLookAndFeel();
    }

    private void setLookAndFeel() {
        final Color refBG = Color.DARK_GRAY;
        final Color thisBG = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 230);
        setBackground(thisBG);
        setLayout(miglayout);
        miglayout.setLayoutConstraints("insets 0 0 0 0, aligny center");
    }

    public void addActionButton(final ActionBarButton btn) {
        add(btn, "w 40!, h 32!");
        revalidate();
    }

}
