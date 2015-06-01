package magic.ui.deck.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.cardtable.CardTablePanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckLegalityTabPanel extends JPanel {

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardTablePanel.CP_CARD_SELECTED;

    private final CardsLegalityPanel cardsLegalityPanel;
    private final FormatsLegalityPanel formatsLegalityPanel;
    private final MigLayout miglayout = new MigLayout();    
    private MagicDeck deck = new MagicDeck();

    public DeckLegalityTabPanel() {

        formatsLegalityPanel = new FormatsLegalityPanel();
        cardsLegalityPanel = new CardsLegalityPanel();
        
        setPropertyChangeListeners();

        setLookAndFeel();
        refreshLayout();

    }

    private void setPropertyChangeListeners() {
        cardsLegalityPanel.addPropertyChangeListener(CardsLegalityPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        cardsLegalityPanel.addPropertyChangeListener(
                CardsLegalityPanel.CP_CARD_DCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CardsLegalityPanel.CP_CARD_DCLICKED, false, true);
                    }
                });
        formatsLegalityPanel.addPropertyChangeListener(FormatsLegalityPanel.CP_FORMAT_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        cardsLegalityPanel.setDeck(deck, formatsLegalityPanel.getSelectedFormat());
                    }
                });
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0");
        add(formatsLegalityPanel, "w 280!, h 100%");
        add(cardsLegalityPanel, "w 100%, h 100%");
        revalidate();
    }

    public MagicCardDefinition getSelectedCard() {
        return cardsLegalityPanel.getSelectedCard();
    }

    void setDeck(final MagicDeck aDeck) {
        this.deck = aDeck;
        formatsLegalityPanel.setDeck(aDeck);
    }

}
