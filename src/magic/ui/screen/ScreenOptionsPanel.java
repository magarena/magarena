package magic.ui.screen;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import magic.data.MagicIcon;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.widget.ActionBarButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class ScreenOptionsPanel extends JPanel {

    protected static final ImageIcon MENU_ICON = ImageHelper.getRecoloredIcon(
        MagicIcon.OPTION_MENU, Color.BLACK, Color.WHITE
    );

    protected final ActionBarButton menuButton;
    protected final ActionBarButton closeButton;

    protected boolean isMenuOpen = false;

    protected abstract void setLayout();

    public ScreenOptionsPanel() {

        menuButton = new ActionBarButton(MENU_ICON, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doToggleMenuOptions();
            }
        });

        closeButton = new ActionBarButton(MENU_ICON, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doToggleMenuOptions();
            }
        });

        setLayout(new MigLayout(
            "flowy, wrap 2, gap 0 2, insets 0 0 2 0, ax right, ay center"
        ));

        setOpaque(false);
    }

    private void doToggleMenuOptions() {
        isMenuOpen = !isMenuOpen;
        setLayout();
    }
}
