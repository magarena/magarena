package magic.ui.widget.cards.table;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import magic.model.MagicCardDefinition;
import magic.ui.screen.decks.ICardsTableListener;

@SuppressWarnings("serial")
public class DeckViewTablePanel extends CardsTablePanel {

    private final List<ICardsTableListener> listeners = new ArrayList<>();
    private boolean isSelectionAdjusting = false;
    private boolean ignoreListSelectionListener;

    public DeckViewTablePanel(List<MagicCardDefinition> defs) {
        super(defs);
        tableModel.showCardCount(true);
        setCardSelectedListener();
        setMouseClickListener();
    }

    public void setListeners(ICardsTableListener... listeners) {
        this.listeners.clear();
        this.listeners.addAll(Arrays.asList(listeners));
    }

    private void setCardSelectedListener() {
        table.getSelectionModel().addListSelectionListener((ev) -> {
            isSelectionAdjusting = ev.getValueIsAdjusting();
            if (!ignoreListSelectionListener && !isSelectionAdjusting) {
                ListSelectionModel lsm = table.getSelectionModel();
                // If cell selection is enabled, both row and column change events are fired
                if (ev.getSource() == lsm && table.getRowSelectionAllowed()) {
                    notifyCardSelected(tableModel.getCardDef(lsm.getLeadSelectionIndex()));
                }
            }
        });
    }

    private void setMouseClickListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isSelectionAdjusting) {
                    int mouseRow = table.rowAtPoint(e.getPoint());
                    MagicCardDefinition card = tableModel.getCardDef(mouseRow);
                    card = card == null ? MagicCardDefinition.UNKNOWN : card;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        notifyOnLeftClick(card);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        notifyOnRightClick(card);
                    }
                }
            }
        });
    }

    @Override
    protected MouseAdapter getRowMouseOverListener() {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                final Point p = e.getPoint();
                final int row = table.rowAtPoint(p);
                final MagicCardDefinition card = tableModel.getCardDef(row);
                notifyCardSelected(card);
            }
        };
    }

    public void setCards(final List<MagicCardDefinition> defs) {
        ignoreListSelectionListener = true;
        tableModel.setCards(defs);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        ignoreListSelectionListener = false;
        reselectLastCards();
    }

    private void notifyCardSelected(MagicCardDefinition card) {
        for (ICardsTableListener listener : listeners) {
            SwingUtilities.invokeLater(() -> {
                listener.onCardSelected(card == null ? MagicCardDefinition.UNKNOWN : card);
            });
        }
    }

    private void notifyOnLeftClick(MagicCardDefinition card) {
        for (ICardsTableListener listener : listeners) {
            SwingUtilities.invokeLater(() -> {
                listener.onLeftClick(card);
            });
        }
    }

    private void notifyOnRightClick(MagicCardDefinition card) {
        for (ICardsTableListener listener : listeners) {
            SwingUtilities.invokeLater(() -> {
                listener.onRightClick(card);
            });
        }
    }

    private void scrollRowToViewportCenter(int row) {

        JViewport viewport = (JViewport) table.getParent();
        Rectangle viewRect = viewport.getViewRect();
        Rectangle rect = table.getCellRect(row, 0, true);

        int y = rect.y < viewRect.y || rect.y > (viewRect.y + viewRect.height)
            ? rect.y - (viewRect.height / 2) + rect.height
            : viewRect.y;

        viewport.setViewPosition(new Point(viewRect.x, y));
    }

    public void setSelectedCard(MagicCardDefinition card) {
        final int index = tableModel.findCardIndex(card);
        if (index != -1) {
            table.getSelectionModel().addSelectionInterval(index, index);
            scrollRowToViewportCenter(index);
        } else if (tableModel.getRowCount() > 0) {
            table.getSelectionModel().addSelectionInterval(0, 0);
        }
    }

    public MagicCardDefinition getSelectedCard() {
        ListSelectionModel lsm = table.getSelectionModel();
        return tableModel.getCardDef(lsm.getLeadSelectionIndex());
    }
}
