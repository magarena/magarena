package magic.ui.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.MenuButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AboutScreen extends AbstractScreen implements IStatusBar, IActionBar, IWikiPage {

    // translatable strings
    private static final String _S1 = "About...";
    private static final String _S2 = "Close";
    private static final String _S3 = "Memory (megabytes)";
    private static final String _S5 = "Used: %.1f";
    private static final String _S6 = "Free: %.1f";
    private static final String _S7 = "Total: %.1f";
    private static final String _S8 = "Max: %.1f";

    final JLabel memoryLabel = new JLabel();

    public AboutScreen() {
        setContent(getContentPanel());
        new Timer(1000, (ActionEvent e) -> { refreshMemoryLabel(); }).start();
        refreshMemoryLabel();
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
        return UiString.get(_S1);
    }

    private static String getHeapUtilizationStats() {
        final float mb = 1024*1024;
        final Runtime runtime = Runtime.getRuntime();
        String s1 = UiString.get(_S5, (runtime.totalMemory() - runtime.freeMemory()) / mb);
        String s2 = UiString.get(_S6, runtime.freeMemory() / mb);
        String s3 = UiString.get(_S7, runtime.totalMemory() / mb);
        String s4 = UiString.get(_S8, runtime.maxMemory() / mb);
        return s1 + " • " + s2 + " • " + s3 + " • " + s4;
    }

    private void refreshMemoryLabel() {
        memoryLabel.setText(getHeapUtilizationStats());
    }

    @Override
    public JPanel getStatusPanel() {
        JLabel lbl = new JLabel(UiString.get(_S3));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
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
        return MenuButton.getCloseScreenButton(UiString.get(UiString.get(_S2)));
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
