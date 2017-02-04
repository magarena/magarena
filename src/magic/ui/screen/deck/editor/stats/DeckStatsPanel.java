package magic.ui.screen.deck.editor.stats;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.FontsAndBorders;
import magic.ui.screen.deck.editor.IDeckEditorView;
import magic.ui.screen.stats.IPagination;
import magic.ui.screen.stats.PaginationPanel;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.M.MScrollPane;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckStatsPanel extends TexturedPanel
    implements IDeckEditorView, IPagination {

    private GameStatsTableModel tm;
    private GameStatsJTable table;
    private final MScrollPane scrollpane = new MScrollPane();
    private final PaginationPanel paginator;
    private MagicDeck deck;

    public DeckStatsPanel(MagicDeck deck) {
        this.deck = deck;
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        scrollpane.setOpaque(false);
        scrollpane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        paginator = new PaginationPanel(this);
        paginator.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        setLayout(new MigLayout("flowy, insets 0", "[fill, grow]", "[fill, grow]0[]"));
        add(scrollpane.component());
        add(paginator);
    }

    private void refreshStatsTable() {
        tm = new GameStatsTableModel(deck);
    }

    private void refreshStatsTable(int page) {
        tm = new GameStatsTableModel(deck, page);
    }

    @Override
    public void notifyShowing() {
        if (GeneralConfig.getInstance().isGameStatsEnabled()) {
            if (tm == null) {
                refreshStatsTable();
            }
            if (tm != null) {
                table = new GameStatsJTable(tm);
                scrollpane.setViewportView(table);
                paginator.refresh();
            }
        }
    }

    void doShowFirstPage() {
        refreshStatsTable(0);
        if (table != null) {
            table.setModel(tm);
        }
    }

    void doShowPreviousPage() {
        refreshStatsTable(tm.getPrevPageNum());
        if (table != null) {
            table.setModel(tm);
        }
    }

    void doShowNextPage() {
        refreshStatsTable(tm.getNextPageNum());
        if (table != null) {
            table.setModel(tm);
        }
    }

    void doShowLastPage() {
        refreshStatsTable(tm.getTotalPages() - 1);
        if (table != null) {
            table.setModel(tm);
        }
    }

    @Override
    public int getPageNum() {
        return tm != null ? tm.getPageNum() : 0;
    }

    @Override
    public int getTotalPages() {
        return tm != null ? tm.getTotalPages() : 0;
    }

    @Override
    public boolean hasPrevPage() {
        return tm != null ? tm.hasPrevPage() : false;
    }

    @Override
    public boolean hasNextPage() {
        return tm != null ? tm.hasNextPage() : false;
    }

    @Override
    public void doPlusButtonAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doMinusButtonAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ActionBarButton> getActionButtons() {
        return new ArrayList<>();
    }

    @Override
    public MagicCardDefinition getSelectedCard() {
        return null;
    }

    private void setTableModel() {
        if (table != null) {
            table.setModel(tm);
        }
    }

    private int getPrevPageNum() {
        return tm.getPrevPageNum();
    }

    private int getNextPageNum() {
        return tm.getNextPageNum();
    }

    @Override
    public void displayFirstPage() {
        refreshStatsTable(1);
        setTableModel();
    }

    @Override
    public void displayPreviousPage() {
        refreshStatsTable(getPrevPageNum());
        setTableModel();
    }

    @Override
    public void displayNextPage() {
        refreshStatsTable(getNextPageNum());
        setTableModel();
    }

    @Override
    public void displayLastPage() {
        refreshStatsTable(getTotalPages());
        setTableModel();
    }

    public void setDeck(MagicDeck deck) {
        this.deck = deck;
        this.tm = null;
    }
}
