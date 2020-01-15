package magic.ui.widget.deck.legality;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.screen.deck.editor.IDeckEditorView;
import magic.ui.screen.decks.ICardsTableListener;
import magic.ui.screen.decks.IDeckView;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class LegalityPanel extends JPanel
    implements IDeckEditorView, IDeckView {

    // translatable strings
    private static final String _S1 = "Legal";
    private static final String _S2 = "Illegal";
    private static final String _S3 = "Banned";
    private static final String _S4 = "Restricted";
    private static final String _S5 = "Only one copy of a card allowed in the deck.";
    private static final String _S6 = "Too many copies";
    private static final String _S7 = "Too many copies of a card in deck.";
    private static final String _S8 = "Does not apply to basic lands,";
    private static final String _S9 = "or";

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = "c5f420c3-dc1c-4d1b-a07b-0d055716207d";

    private static final JPanel HELP_PANEL = new LegalityLegendPanel();

    private final CardsLegalityPanel cardsLegalityPanel;
    private final FormatsLegalityPanel formatsLegalityPanel;
    private final MigLayout miglayout = new MigLayout();
    private MagicDeck deck = new MagicDeck();

    public LegalityPanel() {

        formatsLegalityPanel = new FormatsLegalityPanel();
        cardsLegalityPanel = new CardsLegalityPanel();

        setPropertyChangeListeners();

        setLookAndFeel();
        refreshLayout();

    }

    private void setPropertyChangeListeners() {
        cardsLegalityPanel.addPropertyChangeListener(
            CardsLegalityPanel.CP_CARD_SELECTED,
                evt -> firePropertyChange(CP_CARD_SELECTED, false, true));
        formatsLegalityPanel.addPropertyChangeListener(
            FormatsLegalityPanel.CP_FORMAT_SELECTED,
                evt -> cardsLegalityPanel.setDeck(deck, formatsLegalityPanel.getSelectedFormat()));
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
        HELP_PANEL.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0, wrap 2");
        add(HELP_PANEL, "w 100%, spanx 2");
        add(formatsLegalityPanel, "w 280!, h 100%");
        add(cardsLegalityPanel, "w 100%, h 100%");
        revalidate();
    }

    @Override
    public MagicCardDefinition getSelectedCard() {
        return cardsLegalityPanel.getSelectedCard();
    }

    public void setDeck(final MagicDeck aDeck) {
        this.deck = aDeck;
    }

    @Override
    public void doPlusButtonAction() {
        throw new UnsupportedOperationException("doPlusButtonAction() not implemented.");
    }

    @Override
    public void doMinusButtonAction() {
        throw new UnsupportedOperationException("doMinusButtonAction() not implemented.");
    }

    @Override
    public List<ActionBarButton> getActionButtons() {
        return new ArrayList<>();
    }

    @Override
    public void notifyShowing() {
        formatsLegalityPanel.setDeck(deck);
    }

    private static class LegalityLegendPanel extends TexturedPanel {

        public LegalityLegendPanel() {

            setPreferredSize(new Dimension(getWidth(), 30));
            setMinimumSize(getPreferredSize());

            setLayout(new MigLayout("gapx 20, aligny center"));

            add(getIconLabel(MagicIcon.LEGAL, MText.get(_S1)));
            add(getIconLabel(MagicIcon.ILLEGAL, MText.get(_S2)));
            add(getIconLabel(MagicIcon.BANNED, MText.get(_S3)));
            add(getIconLabel(MagicIcon.RESTRICTED, MText.get(_S4), MText.get(_S5)));
            String unlimitedList = MagicDeckConstructionRule.unlimitedCardList("</i> " + MText.get(_S9) + " <i>");
            add(getIconLabel(MagicIcon.RESTRICTED, MText.get(_S6),
                    String.format("<html><b>%s</b><br>%s<br><i>%s</i>.</html>",
                            MText.get(_S7),
                            MText.get(_S8),
                            unlimitedList)
            ));
        }

        private JLabel getIconLabel(MagicIcon magicIcon, String text, String tooltip) {
            final JLabel lbl = new JLabel(text);
            lbl.setIcon(MagicImages.getIcon(magicIcon));
            lbl.setToolTipText(tooltip);
            return lbl;
        }

        private JLabel getIconLabel(MagicIcon magicIcon, String text) {
            return getIconLabel(magicIcon, text, null);
        }

    }

    public void setCardsTableListeners(ICardsTableListener[] listeners) {
        cardsLegalityPanel.setCardsTableListeners(listeners);
    }

}
