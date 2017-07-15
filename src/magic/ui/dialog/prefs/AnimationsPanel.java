package magic.ui.dialog.prefs;

import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;
import magic.translate.MText;
import magic.ui.mwidgets.MCheckBox;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class AnimationsPanel extends JPanel {

    // translatable strings.
    private static final String _S1 = "New turn delay:";
    private static final String _S2 = "If the 'New turn message' option is switched on then this setting determines how long (in seconds) it should be displayed. Left-click or space key cancels the delay.";
    private static final String _S3 = "Non-land preview:";
    private static final String _S4 = "When playing a non-land card, this setting determines how long (in seconds) it should be displayed at full size. Left-click or space key cancels preview.";
    private static final String _S5 = "Land preview:";
    private static final String _S6 = "When playing a land card, this setting determines how long (in seconds) it should be displayed at full size. Left-click or space key cancels preview.";
    private static final String _S70 = "Play animations / effects";
    private static final String _S71 = "Turning animations off will speed up gameplay but make it harder to follow the action.";

    private final static GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MCheckBox animateCheckBox;
    private final AnimationFlagsPanel flagsPanel;
    private final SliderPanel landPreviewSlider;
    private final SliderPanel nonlandPreviewSlider;
    private final SliderPanel newTurnAlertSlider;

    AnimationsPanel(final MouseListener aListener) {

        flagsPanel = new AnimationFlagsPanel(aListener);
        landPreviewSlider = getLandPreviewSlider(aListener);
        nonlandPreviewSlider = getNonLandPreviewSlider(aListener);
        newTurnAlertSlider = getNewTurnAlertSlider(aListener);
        JPanel subPanel = getAnimationSubPanel();
        animateCheckBox = getAnimateCheckbox(aListener, subPanel);

        setLayout(new MigLayout("flowy, insets 16, gapy 6"));
        add(animateCheckBox.component());
        add(subPanel, "w 100%");

    }

    private JPanel getAnimationSubPanel() {
        final JPanel panel = new SubPanel();
        panel.setEnabled(GeneralConfig.get(BooleanSetting.ANIMATE_GAMEPLAY));
        return panel;
    }

    private MCheckBox getAnimateCheckbox(MouseListener aListener, JPanel panel) {
        final MCheckBox cb = new MCheckBox(MText.get(_S70), GeneralConfig.get(BooleanSetting.ANIMATE_GAMEPLAY));
        cb.setToolTipText(MText.get(_S71));
        cb.setFocusable(false);
        cb.addMouseListener(aListener);
        cb.addChangeListener((ChangeEvent e) -> {
            panel.setEnabled(animateCheckBox.isSelected());
        });
        return cb;
    }

    private SliderPanel getNewTurnAlertSlider(MouseListener aListener) {
        final SliderPanel sp = new SliderPanel(MText.get(_S1), 1, 10, 1, CONFIG.getNewTurnAlertDuration() / 1000);
        sp.setToolTipText(MText.get(_S2));
        sp.addMouseListener(aListener);
        sp.setFontBold(false);
        return sp;
    }

    private SliderPanel getNonLandPreviewSlider(MouseListener aListener) {
        final SliderPanel sp = new SliderPanel(MText.get(_S3), 1, 20, 1, CONFIG.getNonLandPreviewDuration() / 1000);
        sp.setToolTipText(MText.get(_S4));
        sp.addMouseListener(aListener);
        sp.setFontBold(false);
        return sp;
    }

    private SliderPanel getLandPreviewSlider(MouseListener aListener) {
        final SliderPanel sp = new SliderPanel(MText.get(_S5), 1, 20, 1, CONFIG.getLandPreviewDuration() / 1000);
        sp.setToolTipText(MText.get(_S6));
        sp.addMouseListener(aListener);
        sp.setFontBold(false);
        return sp;
    }

    void saveSettings() {
        GeneralConfig.set(BooleanSetting.ANIMATE_GAMEPLAY, animateCheckBox.isSelected());
        CONFIG.setNewTurnAlertDuration(newTurnAlertSlider.getValue() * 1000);
        CONFIG.setLandPreviewDuration(landPreviewSlider.getValue() * 1000);
        CONFIG.setNonLandPreviewDuration(nonlandPreviewSlider.getValue() * 1000);
        flagsPanel.saveSettings();
    }

    private class SubPanel extends JPanel {

        SubPanel() {
            setLayout(new MigLayout("flowy, insets 4 20 0 0, gapy 6"));
            add(flagsPanel, "w 100%");
            add(landPreviewSlider, "w 100%, gaptop 6");
            add(nonlandPreviewSlider, "w 100%");
            add(newTurnAlertSlider, "w 100%");
        }

        @Override
        public void setEnabled(boolean b) {
            for (Component c : getComponents()) {
                c.setEnabled(b);
            }
            super.setEnabled(b);
        }
    }

}
