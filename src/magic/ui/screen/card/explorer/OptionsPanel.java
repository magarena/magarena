package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.ui.FontsAndBorders;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.BigDialButton;
import magic.ui.widget.cards.table.CardsTableStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class OptionsPanel extends JPanel {

    private static final Icon MENU_ICON =
            ImageHelper.getRecoloredIcon(MagicIcon.OPTION_MENU, Color.BLACK, Color.WHITE);

    private static final Icon CLOSE_MENU =
            ImageHelper.getRecoloredIcon(MagicIcon.CLOSE, Color.BLACK, Color.WHITE);

    private boolean isMenuOpen = false;
    private final BigDialButton layoutButton;
    private final BigDialButton styleButton;
    private final ActionBarButton menuButton;
    private final ActionBarButton closeButton;

    OptionsPanel(final ExplorerScreen listener) {

        layoutButton = new BigDialButton(
                ExplorerScreenLayout.values().length,
                ExplorerScreenLayout.getLayout().ordinal(),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.doSwitchLayout();
                    }
                }
        );

        styleButton = new BigDialButton(
                CardsTableStyle.values().length,
                CardsTableStyle.getStyle().ordinal(),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.doSwitchStyle();
                    }
                }
        );

        menuButton = new ActionBarButton((ImageIcon) MENU_ICON, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doToggleMenuOptions();
            }
        });

        closeButton = new ActionBarButton((ImageIcon) CLOSE_MENU, new AbstractAction() {
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
            add(getLabel("Layout"), "ax center");
            add(layoutButton, "h 24!, gapbottom 2");
            add(getLabel("Style"), "ax center");
            add(styleButton, "h 24!, gapbottom 2");
            add(closeButton, "spany 2, gapbottom 2");
        } else {
            add(menuButton, "spany 2");            
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
}
