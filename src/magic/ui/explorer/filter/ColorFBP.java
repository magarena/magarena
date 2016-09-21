package magic.ui.explorer.filter;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.MagicUI;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
class ColorFBP extends FilterButtonPanel {

    private class FilterValuesPanel extends JPanel {

        private final JCheckBox[] checkboxes;

        FilterValuesPanel() {
            checkboxes = new JCheckBox[MagicColor.values().length];
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(FontsAndBorders.DOWN_BORDER);
            setOpaque(false);
            for (int i = 0; i < MagicColor.NR_COLORS; i++) {
                final MagicColor color = MagicColor.values()[i];
                final JPanel colorPanel = new JPanel();
                colorPanel.setOpaque(false);
                final JCheckBox cb = new JCheckBox("", false);
                cb.setOpaque(false);
                cb.setFocusPainted(true);
                cb.setAlignmentY(Component.CENTER_ALIGNMENT);
                cb.setActionCommand(Character.toString(color.getSymbol()));
                cb.addActionListener((e) -> {
                    MagicUI.showBusyCursorFor(cb);
                    ColorFBP.this.filterChanged();
                    MagicUI.showDefaultCursorFor(cb);
                });
                checkboxes[i] = cb;
                colorPanel.add(checkboxes[i]);
                colorPanel.add(new JLabel(MagicImages.getIcon(color.getManaType())));
                add(colorPanel);
            }
        }

        private JCheckBox[] getCheckboxes() {
            return checkboxes;
        }

        private boolean hasSelectedCheckbox() {
            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) {
                    return true;
                }
            }
            return false;
        }
    }

    // translatable strings
    private static final String _S11 = "Color";

    private final FilterValuesPanel valuesPanel;

    ColorFBP(IFilterListener aListener) {
        super(UiString.get(_S11));
        this.filterListener = aListener;
        this.valuesPanel = new FilterValuesPanel();
        setPopupContent();
    }

    @Override
    protected JCheckBox[] getCheckboxes() {
        return valuesPanel.getCheckboxes();
    }

    @Override
    protected JComponent getFilterValuesComponent() {
        return valuesPanel;
    }

    @Override
    protected Dimension getPopupDialogSize() {
        return new Dimension(280, 90);
    }

    @Override
    protected IFilterListener getSearchOptionsListener() {
        return filterListener;
    }

    @Override
    protected boolean hideSearchOptionsAND() {
        return false;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasColor(MagicColor.values()[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return valuesPanel.hasSelectedCheckbox();
    }

}
