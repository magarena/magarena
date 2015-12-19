package magic.ui.duel.textmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.duel.SwingGameController;
import magic.ui.IChoiceViewer;
import magic.ui.duel.PermanentViewerInfo;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;

@SuppressWarnings("serial")
class BasicLandPermanentButton extends PanelButton implements IChoiceViewer {

    private final PermanentViewerInfo permanentInfo;
    private final SwingGameController controller;
    private final JPanel landPanel;

    BasicLandPermanentButton(final PermanentViewerInfo permanentInfo,final SwingGameController controller) {

        this.permanentInfo=permanentInfo;
        this.controller=controller;

        landPanel=new JPanel(new BorderLayout());
        landPanel.setOpaque(false);
        landPanel.setBorder(FontsAndBorders.NO_TARGET_BORDER);

        final JLabel manaLabel=new JLabel();
        manaLabel.setHorizontalAlignment(JLabel.CENTER);
        manaLabel.setPreferredSize(new Dimension(0,30));
        manaLabel.setIcon(MagicImages.getIcon(permanentInfo.manaColor));
        landPanel.add(manaLabel,BorderLayout.CENTER);

        final JLabel tappedLabel = new JLabel(permanentInfo.tapped ? MagicImages.getIcon(MagicIcon.MANA_TAP) : null);
        tappedLabel.setPreferredSize(new Dimension(0,16));
        landPanel.add(tappedLabel,BorderLayout.SOUTH);

        setComponent(landPanel);
        showValidChoices(controller.getValidChoices());
    }

    @Override
    public void mouseClicked() {

        controller.processClick(permanentInfo.permanent);
    }

    @Override
    public void mouseEntered() {}

    @Override
    public void showValidChoices(final Set<?> validChoices) {

        setValid(validChoices.contains(permanentInfo.permanent));
    }

    @Override
    public Color getValidColor() {

        return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
    }
}
