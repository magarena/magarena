package magic.ui.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.MenuButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AboutScreen extends AbstractScreen implements IStatusBar, IActionBar, IWikiPage {

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

    private static String getHeapUtilizationStats() {
        final float mb = 1024*1024;
        final Runtime runtime = Runtime.getRuntime();
        return String.format("Used: %.1f • Free: %.1f • Total: %.1f • Max: %.1f",
            (runtime.totalMemory() - runtime.freeMemory()) / mb,
            runtime.freeMemory() / mb,
            runtime.totalMemory() / mb,
            runtime.maxMemory() / mb
        );
    }

    @Override
    public JPanel getStatusPanel() {
        JLabel lbl = new JLabel("Memory (megabytes)");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JLabel memoryLabel = new JLabel(getHeapUtilizationStats());
        memoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        memoryLabel.setForeground(Color.WHITE);
        memoryLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JPanel panel = new JPanel(new MigLayout("flowy"));
        panel.setOpaque(false);
        panel.add(lbl, "w 100%, h 100%");
        panel.add(memoryLabel, "w 100%, h 100%");
        return panel;
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

    @Override
    public String getWikiPageName() {
        return "home";
    }

}
