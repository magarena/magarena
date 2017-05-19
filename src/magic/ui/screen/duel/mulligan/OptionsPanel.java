package magic.ui.screen.duel.mulligan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.HandZoneLayout;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.BigDialButton;
import magic.ui.screen.widget.IDialButtonHandler;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class OptionsPanel extends JPanel
    implements IDialButtonHandler {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Layout";

    private static final ImageIcon MENU_ICON =
            ImageHelper.getRecoloredIcon(MagicIcon.OPTION_MENU, Color.BLACK, Color.WHITE);

    private boolean isMenuOpen = false;
    private final BigDialButton layoutButton;
    private final ActionBarButton menuButton;
    private final ActionBarButton closeButton;
    private final MulliganScreen screen;

    OptionsPanel(final MulliganScreen screen) {

        this.screen = screen;

        layoutButton = new BigDialButton(this);

        menuButton = new ActionBarButton(MENU_ICON, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doToggleMenuOptions();
            }
        });

        closeButton = new ActionBarButton((ImageIcon) MENU_ICON, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doToggleMenuOptions();
            }
        });

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
            add(layoutButton, "ax center, h 24!, w 24!, gapbottom 2");
            add(closeButton, "spany 2, h 32!, w 32!");
        } else {
            add(menuButton, "spany 2, h 32!, w 32!");
        }
        revalidate();
        repaint();
    }

    private void doToggleMenuOptions() {
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
        return HandZoneLayout.values().length;
    }

    @Override
    public int getDialPosition() {
        return HandZoneLayout.getLayout().ordinal();
    }

    @Override
    public boolean doLeftClickAction(int dialPosition) {
        screen.setCardsLayout(dialPosition);
        return true;
    }

    @Override
    public boolean doRightClickAction(int dialPosition) {
        screen.setCardsLayout(dialPosition);
        return true;
    }

    @Override
    public void onMouseEntered(int dialPosition) {
        screen.flashLayoutSetting();
    }
}
