package magic.ui.explorer.filter;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.ui.MagicUI;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class FilterButtonPanel extends JPanel
    implements IFilterListener {

    // prevents multiple updates of the cards list when resetting multiple
    // filters values at once via reset().
    private boolean ignoreFilterChanges;

    private FilterButton filterButton;
    private final ClickPreventer clickPreventer = new ClickPreventer();

    private IFilterListener filterListener;
    protected FilterDialog filterDialog;

    protected abstract boolean isCardValid(final MagicCardDefinition card, final int i);
    protected abstract boolean hasActiveFilterValue();
    protected abstract String getSearchOperandText();
        
    // CTR
    FilterButtonPanel(String title, String tooltip, IFilterListener aListener) {
        this.filterListener = aListener;
        createFilterButton(title, tooltip);
        setLayout();
        setOpaque(false);
    }

    // CTR
    FilterButtonPanel(String title, IFilterListener aListener) {
        this(title, null, aListener);
    }

    // CTR
    FilterButtonPanel() {
        setOpaque(false);
    }

    private void setLayout() {
        setLayout(new MigLayout("insets 0, fill, hidemode 3"));
        add(filterButton, "grow");
    }

    private void createFilterButton(String title, String tooltip) {
        filterButton = new FilterButton(title, tooltip);
        filterButton.addActionListener((e) -> {
            if (clickPreventer.isNotRunning()) {
                MagicUI.showBusyCursorFor(filterButton);
                showFilterDialog();
                MagicUI.showDefaultCursorFor(filterButton);
            }
        });
    }

    private void setEscapeKeyAction() {
        JRootPane root = filterDialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "ESC");
        root.getActionMap().put("ESC", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
               hideFilterDialog();
            }
        });
    }

    private void showFilterDialog() {
        if (filterDialog == null) {
            setFilterDialog();
        }
        if (!filterDialog.isVisible()) {
            // set location relative to button
            final Point location = this.getLocation();
            SwingUtilities.convertPointToScreen(location, this.getParent());
            location.translate(0, this.getHeight());
            filterDialog.setLocation(location);
            filterDialog.setVisible(true);
        }
    }

    private void hideFilterDialog() {
        filterDialog.setVisible(false);
        filterButton.setActiveFlag(hasActiveFilterValue());
    }

    void resetStayOpen() {
        ignoreFilterChanges = true;
        filterDialog.reset();
        filterButton.setToolTipText(null);
        filterButton.setActiveFlag(false);
        ignoreFilterChanges = false;
        filterListener.filterChanged();
    }

    void reset() {
        if (filterDialog != null) {
            ignoreFilterChanges = true;
            filterDialog.reset();
            filterButton.setToolTipText(null);
            hideFilterDialog();
            ignoreFilterChanges = false;
        }
    }

    protected MigLayout getFilterDialogLayout() {
        return new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow][50!, fill]");
    }

    protected Dimension getFilterDialogSize() {
        return new Dimension(260, 300);
    }

    protected boolean hideSearchOptionsAND() {
        return false;
    }

    protected String getFilterTooltip() {
        return null;
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

//    protected void setFilterDialog(FilterDialog aDialog) {
//        filterDialog = aDialog;
//        filterDialog.addWindowFocusListener(new WindowAdapter() {
//            @Override
//            public void windowLostFocus(WindowEvent e) {
//                clickPreventer.start();
//                if (filterDialog.isVisible()) {
//                    hideFilterDialog();
//                }
//            }
//        });
//        setEscapeKeyAction();
//    }
    
    protected abstract FilterDialog getFilterDialog();

    private void setFilterDialog() {
        filterDialog = getFilterDialog();
        filterDialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                clickPreventer.start();
                if (filterDialog.isVisible()) {
                    hideFilterDialog();
                }
            }
        });
        setEscapeKeyAction();
    }

    protected boolean matches(MagicCardDefinition aCard) {
        return filterDialog == null ? true : filterDialog.filterMatches(aCard);
    }
}
