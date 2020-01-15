package magic.ui.screen.cardflow;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.screen.ScreenOptionsPanel;
import magic.ui.screen.widget.BigDialButton;
import magic.ui.screen.widget.IDialButtonHandler;

@SuppressWarnings("serial")
class OptionsPanel extends ScreenOptionsPanel {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Scale";
    private static final String _S2 = "Animate";

    private final BigDialButton scaleButton;
    private final BigDialButton animateButton;
    private ImageSizePresets sizePreset;
    private final ScreenSettings settings;
    private final CardFlowScreen screen;

    OptionsPanel(final CardFlowScreen screen, final ScreenSettings settings) {
        this.screen = screen;
        this.settings = settings;
        sizePreset = settings.getImageSizePreset();
        scaleButton = new BigDialButton(getScaleOptionHandler());
        animateButton = new BigDialButton(getAnimateOptionHandler());
        setLayout();
    }

    private void setImageSizePreset(int ordinal) {
        sizePreset = ImageSizePresets.values()[ordinal];
        sizePreset = sizePreset == ImageSizePresets.SIZE_ORIGINAL
            ? ImageSizePresets.SIZE_ORIGINAL.next()
            : sizePreset;
        screen.setImageSize(sizePreset);
    }

    private IDialButtonHandler getScaleOptionHandler() {
        return new IDialButtonHandler() {
            @Override
            public int getDialPositionsCount() {
                // one less since not interested in SIZE_ORIGINAL.
                return ImageSizePresets.values().length - 1;
            }
            @Override
            public int getDialPosition() {
                // dial position one less since not including SIZE_ORIGINAL;
                return sizePreset.ordinal() - 1;
            }
            @Override
            public boolean doLeftClickAction(int dialPosition) {
                setImageSizePreset(dialPosition + 1);
                return true;
            }
            @Override
            public boolean doRightClickAction(int dialPosition) {
                setImageSizePreset(dialPosition + 1);
                return true;
            }
            @Override
            public void onMouseEntered(int position) {
                screen.flashImageSizePreset(sizePreset);
            }
        };
    }

    private IDialButtonHandler getAnimateOptionHandler() {
        return new IDialButtonHandler() {
            private void doDialClickAction(int position) {
                settings.setAnimationEnabled(position == 0);
                screen.setAnimateSetting(settings.isAnimationEnabled());
            }
            @Override
            public int getDialPositionsCount() {
                return 2;
            }
            @Override
            public int getDialPosition() {
                return settings.isAnimationEnabled() ? 0 : 1;
            }
            @Override
            public boolean doLeftClickAction(int dialPosition) {
                doDialClickAction(dialPosition);
                return true;
            }
            @Override
            public boolean doRightClickAction(int dialPosition) {
                doDialClickAction(dialPosition);
                return true;
            }
            @Override
            public void onMouseEntered(int dialPosition) {
                screen.setAnimateSetting(settings.isAnimationEnabled());
            }
        };
    }

    @Override
    protected void setLayout() {
        removeAll();
        if (isMenuOpen) {
            add(getLabel(MText.get(_S2)), "ax center, w 60!");
            add(animateButton, "ax center, h 24!, w 24!, gapbottom 2");
            add(getLabel(MText.get(_S1)), "ax center, w 60!");
            add(scaleButton, "ax center, h 24!, w 24!, gapbottom 2");
            add(closeButton, "spany 2, h 32!, w 32!");
        } else {
            add(menuButton, "spany 2, h 32!, w 32!");
        }
        revalidate();
        repaint();
    }

    private JLabel getLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    void saveSettings() {
        settings.setImageSizePreset(sizePreset);
        settings.save();
    }
}
