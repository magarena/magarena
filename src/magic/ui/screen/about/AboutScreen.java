package magic.ui.screen.about;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.utility.GraphicsUtils;
import magic.utility.WikiPage;
import net.miginfocom.swing.MigLayout;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

@SuppressWarnings("serial")
public class AboutScreen extends AbstractScreen
    implements IStatusBar, IActionBar, IWikiPage, TimelineCallback {

    // translatable strings
    private static final String _S1 = "About...";
    private static final String _S2 = "Close";
    private static final String _S3 = "Memory (megabytes)";
    private static final String _S5 = "Used: %.1f";
    private static final String _S6 = "Free: %.1f";
    private static final String _S7 = "Total: %.1f";
    private static final String _S8 = "Max: %.1f";
    private static final String _S9 = "Magarena License";
    private static final String _S10 = "This program is free software : you can redistribute it and/or<br>modify it under the terms of the GNU General Public License<br>as published by the Free Software Foundation.";
    private static final String _S11 = "Magarena License";
    private static final String _S12 = "Displays the license details.";

    private final JLabel memoryLabel = new JLabel();
    private Timeline dropTimeline;
    private float imageScale = 0.0f;

    public AboutScreen() {
        setContent(getContentPanel());
        new Timer(1000, (ActionEvent e) -> { refreshMemoryLabel(); }).start();
        refreshMemoryLabel();
        doDropAnimation();
    }

    private void doDropAnimation() {
        dropTimeline = new Timeline(this);
        dropTimeline.addCallback(this);
        dropTimeline.addPropertyToInterpolate("ImageScale", 6f, 1f);
        dropTimeline.setDuration(500);
        dropTimeline.play();
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

    public void setImageScale(float f) {
        this.imageScale = f;
        repaint();
    }

    public float getImageScale() {
        return this.imageScale;
    }

    private void drawMagarenaImage(Graphics g) {
        BufferedImage image = MagicImages.ABOUT_LOGO;
        int scaledW = (int) (image.getWidth() * imageScale);
        int scaledH = (int) (image.getHeight() * imageScale);
        BufferedImage scaled = GraphicsUtils.scale(image, scaledW, scaledH);
        int posX = (getWidth() - scaled.getWidth()) / 2;
        int posY = (getHeight() - scaled.getHeight()) / 2;
        g.drawImage(scaled, posX, posY, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (imageScale > 0) {
            drawMagarenaImage(g);
        }
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
        JPanel panel = new JPanel(new MigLayout("flowy, insets 0, gapy 0"));
        panel.setOpaque(false);
        panel.add(lbl, "w 100%");
        panel.add(memoryLabel, "w 100%");
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
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new ActionBarButton(
                MagicImages.getIcon(MagicIcon.SCROLL_ICON),
                UiString.get(_S11), UiString.get(_S12),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ScreenController.showInfoMessage(String.format("<html><b>%s</b><br>%s<html>",
                            UiString.get(_S9),
                            UiString.get(_S10))
                        );
                    }
                }
            )
        );
        return buttons;
    }

    @Override
    public String getWikiPageName() {
        return WikiPage.HOME;
    }

    @Override
    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
        if (newState == Timeline.TimelineState.DONE) {
            MagicSound.BOOM.play();
        }
    }

    @Override
    public void onTimelinePulse(float durationFraction, float timelinePosition) {
        // not interested.
    }

}
