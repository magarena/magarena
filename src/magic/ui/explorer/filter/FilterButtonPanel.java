package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.ui.theme.ThemeFactory;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class FilterButtonPanel extends JPanel
    implements IFilterListener {

    static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
    protected static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);

    // prevents multiple updates of the cards list when resetting multiple
    // filters values at once via reset().
    private boolean ignoreFilterChanges;

    private final ClickPreventer clickPreventer = new ClickPreventer();
    private FilterButton filterButton;
    private final FilterOptionsPanel filterOptionsPanel;

    protected IFilterListener filterListener;
    protected JDialog popupDialog;

    protected abstract JCheckBox[] getCheckboxes();
    protected abstract boolean isCardValid(final MagicCardDefinition card, final int i);

    // CTR
    FilterButtonPanel(String title, String tooltip) {
        this.filterOptionsPanel = new FilterOptionsPanel(this);
        createFilterButton(title, tooltip);
        createFilterPopupDialog();
        setEscapeKeyAction();
        setLayout();
        setOpaque(false);
    }

    // CTR
    FilterButtonPanel(String title) {
        this(title, null);
    }

    // CTR
    public FilterButtonPanel() {
        this.filterOptionsPanel = null;
        setOpaque(false);
    }

    private void setLayout() {
        setLayout(new MigLayout("insets 0, fill, hidemode 3", "fill", "fill"));
        add(filterButton);
    }

    private void createFilterPopupDialog() {
        this.popupDialog = new FilterPopupDialog();
        this.popupDialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                clickPreventer.start();
                if (popupDialog.isVisible()) {
                    hidePopup();
                }
            }
        });
    }

    private void createFilterButton(String title, String tooltip) {
        filterButton = new FilterButton(title, tooltip);
        filterButton.addActionListener((e) -> {
            doFilterButtonClickedAction();
        });
    }

    private void doFilterButtonClickedAction() {
        if (clickPreventer.isNotRunning()) {
            showPopup();
        }
    }

    private void setEscapeKeyAction() {
        JRootPane root = popupDialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");

        @SuppressWarnings("serial")
        final AbstractAction closeAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
               hidePopup();
            }
        };
        root.getActionMap().put("closeDialog", closeAction);
    }

    private void showPopup() {
        if (!popupDialog.isVisible()) {
            // set location relative to button
            final Point location = this.getLocation();
            SwingUtilities.convertPointToScreen(location, this.getParent());
            location.translate(0, this.getHeight());
            popupDialog.setLocation(location);
            popupDialog.setVisible(true);
        }
    }

    protected void hidePopup() {
        popupDialog.setVisible(false);
        filterButton.setActiveFlag(isFilterActive());
    }

    void resetStayOpen() {
        ignoreFilterChanges = true;
        for (JCheckBox checkbox : getCheckboxes()) {
            checkbox.setSelected(false);
        }
        filterOptionsPanel.reset();
        filterButton.setToolTipText(null);
        filterButton.setActiveFlag(false);
        ignoreFilterChanges = false;
        filterListener.filterChanged();
    }

    void reset() {
        ignoreFilterChanges = true;
        for (JCheckBox checkbox : getCheckboxes()) {
            checkbox.setSelected(false);
        }
        filterOptionsPanel.reset();
        filterButton.setToolTipText(null);
        hidePopup();
        ignoreFilterChanges = false;
    }

    private boolean containsCard(MagicCardDefinition cardDef) {

        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i=0; i < getCheckboxes().length; i++) {
            if (getCheckboxes()[i].isSelected()) {
                somethingSelected = true;
                if (isCardValid(cardDef, i)) {
                    resultOR = true;
                } else {
                    resultAND = false;
                }
            }
        }
        if (filterOptionsPanel.isSelected(SearchOperand.EXCLUDE)) {
            // exclude selected
            return !resultOR;
        }
        // otherwise return OR or AND result
        return !somethingSelected
            || ((filterOptionsPanel.isSelected(SearchOperand.MATCH_ANY) && resultOR)
            || (filterOptionsPanel.isSelected(SearchOperand.MATCH_ALL) && resultAND));
    }

    boolean doesNotInclude(MagicCardDefinition cardDefinition) {
        return !containsCard(cardDefinition);
    }

    protected boolean isFilterActive() {
        for (JCheckBox cb : getCheckboxes()) {
            if (cb.isSelected()) {
                return true;
            }

        }
        return false;
    }

    protected MigLayout getPopupDialogLayout() {
        return new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow][50!, fill]");
    }

    protected JComponent getFilterValuesComponent() {
        return new JPanel();
    }

    protected Dimension getPopupDialogSize() {
        return new Dimension(260, 300);
    }

    protected IFilterListener getSearchOptionsListener() {
        return filterListener;
    }

    protected boolean hideSearchOptionsAND() {
        return false;
    }

    protected void setPopupContent() {
        popupDialog.setSize(getPopupDialogSize());
        popupDialog.setLayout(getPopupDialogLayout());
        popupDialog.add(getFilterValuesComponent());
        popupDialog.add(filterOptionsPanel);
    }

    abstract protected boolean hasActiveFilterValue();
    
    protected String getFilterTooltip() {
        return null;
    }

    private String getSearchOperandText() {
        return filterOptionsPanel.getSearchOperandText();
    }

    protected String getFilterTooltip(Object[] values, List<Integer> selected) {
        final StringBuilder sb = new StringBuilder();
        if (!selected.isEmpty()) {
            sb.append("<html><b><p>").append(getSearchOperandText()).append("</p></b>");
            for (Integer i : selected) {
                sb.append("• ").append(values[i]).append("<br>");
            }
            sb.append("</html>");
        }
        return sb.toString();
    }

    @Override
    public void filterChanged() {
        if (!ignoreFilterChanges) {
            filterListener.filterChanged();
            filterButton.setActiveFlag(hasActiveFilterValue());
            String tooltip = getFilterTooltip();
            filterButton.setToolTipText(tooltip.isEmpty() ? null : tooltip);
        }
    }

}
