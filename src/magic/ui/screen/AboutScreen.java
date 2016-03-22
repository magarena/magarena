package magic.ui.screen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AboutScreen extends AbstractScreen implements IStatusBar, IActionBar {

    public AboutScreen() {
        setContent(getContentPanel());
    }

    private JPanel getContentPanel() {
        final JPanel panel = new JPanel(new MigLayout("alignx center, aligny center"));
        panel.setOpaque(false);
        return panel;
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage image = MagicImages.ABOUT_LOGO;
        g.drawImage(image,
            (getWidth() - image.getWidth()) / 2,
            (getHeight() - image.getHeight()) / 2,
            null
        );
        super.paintComponent(g);
    }

    @Override
    public String getScreenCaption() {
        return "About...";
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get("Close"));
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

}
