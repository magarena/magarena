package magic.ui.cardpool;

import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import magic.ui.cardtable.CardTablePanel;

@SuppressWarnings("serial")
public class DeckEditorCardPoolPanel extends CardPoolPanel {

    public static final String CP_CARD_RCLICKED = CardTablePanel.CP_CARD_RCLICKED;

    public DeckEditorCardPoolPanel() {
        cardPoolTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_RCLICKED, false, true);
                    }
                });
    }

    @Override
    protected String getCardPoolTitle() {
        final StringBuffer sb = new StringBuffer();
        final int total = cardPool.size();
        sb.append("Cards: ").append(NumberFormat.getInstance().format(total));
        return sb.toString();
    }

    @Override
    public boolean isDeckEditor() {
        return true;
    }

    @Override
    protected MouseAdapter getMouseAdapter() {
        return null;
    }

    @Override
    public boolean requestFocusInWindow() {
        if (cardPoolTable.getSelectedCards().isEmpty()) {
            cardPoolTable.selectFirstRow();
        }
        return super.requestFocusInWindow();
    }

    @Override
    public void refreshTable() {
        refreshContent();
    }

}
