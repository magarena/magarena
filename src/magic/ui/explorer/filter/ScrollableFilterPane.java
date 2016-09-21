package magic.ui.explorer.filter;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import magic.ui.MagicUI;
import static magic.ui.explorer.filter.FilterButtonPanel.POPUP_CHECKBOXES_SIZE;
import static magic.ui.explorer.filter.FilterButtonPanel.TEXT_COLOR;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ScrollableFilterPane extends JScrollPane {

    private class FilterCheckBox extends JCheckBox {

        FilterCheckBox(String text) {
            super(text);
            setOpaque(false);
            setForeground(TEXT_COLOR);
            setFocusPainted(true);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

    }

    private class FilterPanel extends JPanel {

        private final JCheckBox[] checkboxes;

        FilterPanel(Object[] values, IFilterListener aListener) {

            setLayout(new MigLayout("flowy, insets 2, gapy 4"));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setOpaque(false);

            checkboxes = new JCheckBox[values.length];
            for (int i = 0; i < values.length; i++) {
                final JCheckBox cb = new FilterCheckBox(values[i].toString());
                cb.addActionListener((e) -> {
                    MagicUI.showBusyCursorFor(cb);
                    aListener.filterChanged();
                    MagicUI.showDefaultCursorFor(cb);
                });
                checkboxes[i] = cb;
                add(checkboxes[i]);
            }
        }

        JCheckBox[] getCheckboxes() {
            return checkboxes;
        }

        private List<Integer> getSelected() {
            final List<Integer> selected = new ArrayList<>();
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    selected.add(i);
                }
            }
            return selected;
        }
    }

    private final FilterPanel filterPanel;

    ScrollableFilterPane(Object[] values, IFilterListener aListener) {
        this.filterPanel = new FilterPanel(values, aListener);
        setViewportView(filterPanel);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(FontsAndBorders.DOWN_BORDER);
        setOpaque(false);
        getViewport().setOpaque(false);
        setPreferredSize(POPUP_CHECKBOXES_SIZE);
        getVerticalScrollBar().setUnitIncrement(18);
    }

    ScrollableFilterPane(Stream<?> values, IFilterListener aListener) {
        this(values.toArray(), aListener);
    }

    JCheckBox[] getCheckboxes() {
        return filterPanel.getCheckboxes();
    }

    void setMigLayout(MigLayout miglayout) {
        filterPanel.setLayout(miglayout);
    }

    boolean hasSelectedCheckbox() {
        for (JCheckBox cb : filterPanel.getCheckboxes()) {
            if (cb.isSelected()) {
                return true;
            }
        }
        return false;
    }

    List<Integer> getSelected() {
        return filterPanel.getSelected();
    }
}
