package magic.ui.screen.duel.decks;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.screen.ScreenOptionsPanel;
import magic.ui.screen.widget.BigDialButton;
import magic.ui.screen.widget.IDialButtonHandler;
import magic.ui.widget.cards.table.CardsTableStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class OptionsPanel extends ScreenOptionsPanel
    implements IDialButtonHandler {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Style";

    private boolean isMenuOpen = false;
    private final BigDialButton styleButton;
    private final DuelDecksScreen screen;

    OptionsPanel(final DuelDecksScreen screen) {

        this.screen = screen;

        styleButton = new BigDialButton(this);

        setLayout(new MigLayout(
                "flowy, wrap 2, gap 0 2, insets 0 0 2 0, ax right, ay center"
        ));
        setLayout();

        setOpaque(false);
    }

    private void setLayout() {
        removeAll();
        if (isMenuOpen) {
            add(getLabel(MText.get(_S1)), "ax center, w 60!");
            add(styleButton, "ax center, h 24!, w 24!, gapbottom 2");
            add(closeButton, "spany 2, h 32!, w 32!");
        } else {
            add(menuButton, "spany 2, h 32!, w 32!");
        }
        revalidate();
        repaint();
    }

    @Override
    protected void doToggleMenuOptions() {
        isMenuOpen = !isMenuOpen;
        setLayout();
    }

    private JLabel getLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    @Override
    public int getDialPositionsCount() {
        return CardsTableStyle.values().length;
    }

    @Override
    public int getDialPosition() {
        return CardsTableStyle.getStyle().ordinal();
    }

    @Override
    public boolean doLeftClickAction(int dialPosition) {
        screen.setCardsTableStyle(dialPosition);
        return true;
    }

    @Override
    public boolean doRightClickAction(int dialPosition) {
        screen.setCardsTableStyle(dialPosition);
        return true;
    }

    @Override
    public void onMouseEntered(int dialPosition) {
        // not supported
    }
}
