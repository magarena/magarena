package magic.ui.explorer.filter;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
class ColorFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S11 = "Color";

    ColorFBP(ActionListener aListener) {
        super(UiString.get(_S11));
        doLayoutColorPanel(aListener);
    }

    private void doLayoutColorPanel(ActionListener aListener) {

        final JPanel dialogPanel = new DialogContentPanel();

        checkboxes = new JCheckBox[MagicColor.NR_COLORS];
        final JPanel colorsPanel = new JPanel();
        colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.X_AXIS));
        colorsPanel.setBorder(FontsAndBorders.DOWN_BORDER);
        colorsPanel.setOpaque(false);
        dialog.setSize(280, 90);
        for (int i = 0; i < MagicColor.NR_COLORS; i++) {
            final MagicColor color = MagicColor.values()[i];
            final JPanel colorPanel = new JPanel();
            colorPanel.setOpaque(false);
            checkboxes[i] = new JCheckBox("", false);
            checkboxes[i].addActionListener(aListener);
            checkboxes[i].setOpaque(false);
            checkboxes[i].setFocusPainted(true);
            checkboxes[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            checkboxes[i].setActionCommand(Character.toString(color.getSymbol()));
            colorPanel.add(checkboxes[i]);
            colorPanel.add(new JLabel(MagicImages.getIcon(color.getManaType())));
            colorsPanel.add(colorPanel);
        }
        colorsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(colorsPanel);

        final ButtonGroup colorFilterBg = new ButtonGroup();
        radioButtons = new JRadioButton[FILTER_CHOICES.length];
        for (int i = 0; i < FILTER_CHOICES.length; i++) {
            radioButtons[i] = new JRadioButton(FILTER_CHOICES[i]);
            radioButtons[i].addActionListener(aListener);
            radioButtons[i].setOpaque(false);
            radioButtons[i].setForeground(TEXT_COLOR);
            radioButtons[i].setFocusPainted(true);
            radioButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                radioButtons[i].setSelected(true);
            }
            colorFilterBg.add(radioButtons[i]);
            dialogPanel.add(radioButtons[i]);
        }

        dialog.add(dialogPanel);

    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasColor(MagicColor.values()[i]);
    }

}
