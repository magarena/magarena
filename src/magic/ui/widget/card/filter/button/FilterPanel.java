package magic.ui.widget.card.filter.button;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.ui.MouseHelper;
import magic.ui.widget.card.filter.ClickPreventer;
import magic.ui.widget.card.filter.IFilterListener;
import magic.ui.widget.card.filter.dialog.FilterDialog;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class FilterPanel extends JPanel {

    // prevents multiple updates of the cards list when resetting multiple
    // filters values at once via reset().
    private boolean ignoreFilterChanges;

    private FilterButton filterButton;
    private final ClickPreventer clickPreventer = new ClickPreventer();

    private IFilterListener filterListener;
    private FilterDialog filterDialog;

    public abstract boolean matches(MagicCardDefinition aCard);
    protected abstract FilterDialog getFilterDialog();
    protected abstract boolean isFiltering();
    protected abstract String getFilterTooltip();
        
    // CTR
    FilterPanel(String title, String tooltip, IFilterListener aListener) {
        this.filterListener = aListener;
        createFilterButton(title, tooltip);
        setLayout();
        setOpaque(false);
    }

    // CTR
    FilterPanel(String title, IFilterListener aListener) {
        this(title, null, aListener);
    }

    // CTR
    FilterPanel() {
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
                MouseHelper.showBusyCursor(filterButton);
                showFilterDialog();
                MouseHelper.showDefaultCursor(filterButton);
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
        filterButton.setActiveFlag(isFiltering());
    }

    public void resetStayOpen() {
        ignoreFilterChanges = true;
        filterDialog.reset();
        filterButton.setToolTipText(null);
        filterButton.setActiveFlag(false);
        ignoreFilterChanges = false;
        filterListener.filterChanged();
    }

    public void reset() {
        if (filterDialog != null) {
            ignoreFilterChanges = true;
            filterDialog.reset();
            filterButton.setToolTipText(null);
            hideFilterDialog();
            ignoreFilterChanges = false;
        }
    }

    /**
     * Default filter dialog size.
     */
    public Dimension getFilterDialogSize() {
        return new Dimension(260, 300);
    }

    public boolean hideSearchOperandAND() {
        return false;
    }

    public void filterChanged() {
        if (!ignoreFilterChanges) {
            filterListener.filterChanged();
            filterButton.setActiveFlag(isFiltering());
            filterButton.setToolTipText(getFilterTooltip());
        }
    }

    public LayoutManager getFilterPanelLayout() {
        return null;
    };
}
