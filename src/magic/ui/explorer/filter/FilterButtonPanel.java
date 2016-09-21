package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.theme.ThemeFactory;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class FilterButtonPanel extends JPanel
    implements IFilterListener {

    /**
     * Container panel for the search options radio buttons.
     */
    protected class SearchOptionsPanel extends JPanel {

        SearchOptionsPanel(IFilterListener aListener, boolean hideAND) {
            setLayout(new MigLayout("insets 0, gapy 0, flowy"));
            createRadioButtons(aListener, hideAND);
            setOpaque(false);
        }

        private void createRadioButtons(IFilterListener aListener, boolean hideAND) {
            radioButtons = new JRadioButton[FILTER_CHOICES.length];
            final ButtonGroup bg = new ButtonGroup();
            for (int i = 0; i < FILTER_CHOICES.length; i++) {
                final JRadioButton rb = new JRadioButton(FILTER_CHOICES[i]);
                rb.addActionListener((e) -> { aListener.filterChanged(); });
                rb.setOpaque(false);
                rb.setForeground(TEXT_COLOR);
                rb.setFocusPainted(true);
                rb.setAlignmentX(Component.LEFT_ALIGNMENT);
                rb.setSelected(i == 0);
                rb.setVisible(i != 1 || (i == 1 && !hideAND));
                bg.add(rb);
                add(rb, "hidemode 3");
                radioButtons[i] = rb;
            }
        }

        private String getSelectedOptionText() {
            for (JRadioButton rb : radioButtons) {
                if (rb.isSelected()) {
                    return rb.getText();
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    // translatable strings
    private static final String _S1 = "Match any";
    private static final String _S2 = "Match all";
    private static final String _S3 = "Exclude";

    static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
    protected static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);

    protected final String[] FILTER_CHOICES = {
        UiString.get(_S1),
        UiString.get(_S2),
        UiString.get(_S3)
    };

    // prevents multiple updates of the cards list when resetting multiple
    // filters values at once via reset().
    private boolean ignoreFilterChanges;

    private final ClickPreventer clickPreventer = new ClickPreventer();
    private FilterButton filterButton;
    private final SearchOptionsPanel searchOptionsPanel;

    protected IFilterListener filterListener;
    protected JDialog popupDialog;
    protected JRadioButton[] radioButtons;

    protected abstract JCheckBox[] getCheckboxes();
    protected abstract boolean isCardValid(final MagicCardDefinition card, final int i);

    // CTR
    FilterButtonPanel(String title, String tooltip) {
        this.searchOptionsPanel = new SearchOptionsPanel(this, hideSearchOptionsAND());
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
        this.searchOptionsPanel = null;
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

    JRadioButton[] getFilterChoices() {
        return radioButtons;
    }

    void reset() {
        ignoreFilterChanges = true;
        for (JCheckBox checkbox : getCheckboxes()) {
            checkbox.setSelected(false);
        }
        radioButtons[0].setSelected(true);
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
        if (getFilterChoices()[2].isSelected()) {
            // exclude selected
            return !resultOR;
        }
        // otherwise return OR or AND result
        return !somethingSelected
            || ((getFilterChoices()[0].isSelected() && resultOR)
            || (getFilterChoices()[1].isSelected() && resultAND));

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
        return new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow][]");
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
        popupDialog.add(searchOptionsPanel);
    }

    abstract protected boolean hasActiveFilterValue();
    
    protected String getFilterTooltip() {
        return null;
    }

    private String getSearchOptionText() {
        return searchOptionsPanel.getSelectedOptionText();
    }

    protected String getFilterTooltip(Object[] values, List<Integer> selected) {
        final StringBuilder sb = new StringBuilder();
        if (!selected.isEmpty()) {
            sb.append("<html><b><p>").append(getSearchOptionText()).append("</p></b>");
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
