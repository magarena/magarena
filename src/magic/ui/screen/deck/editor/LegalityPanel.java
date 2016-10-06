package magic.ui.screen.deck.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.MagicImages;
import magic.translate.UiString;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LegalityPanel extends JPanel implements IDeckEditorView {

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
    public static final String CP_CARD_DCLICKED = "0dda4041-f44d-4980-8c87-c11cf7b1dc06";

    private static final JPanel HELP_PANEL = new LegalityLegendPanel();

    private final CardsLegalityPanel cardsLegalityPanel;
    private final FormatsLegalityPanel formatsLegalityPanel;
    private final MigLayout miglayout = new MigLayout();
    private MagicDeck deck = new MagicDeck();

    LegalityPanel() {

        formatsLegalityPanel = new FormatsLegalityPanel();
        cardsLegalityPanel = new CardsLegalityPanel();

        setPropertyChangeListeners();

        setLookAndFeel();
        refreshLayout();

    }

    private void setPropertyChangeListeners() {
        cardsLegalityPanel.addPropertyChangeListener(
            CardsLegalityPanel.CP_CARD_SELECTED,
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
                    firePropertyChange(CP_CARD_DCLICKED, false, true);
                }
            });
        formatsLegalityPanel.addPropertyChangeListener(
            FormatsLegalityPanel.CP_FORMAT_SELECTED,
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

    void setDeck(final MagicDeck aDeck) {
        this.deck = aDeck;
        formatsLegalityPanel.setDeck(aDeck);
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

    private static class LegalityLegendPanel extends TexturedPanel {

        public LegalityLegendPanel() {

            setPreferredSize(new Dimension(getWidth(), 30));
            setMinimumSize(getPreferredSize());

            setLayout(new MigLayout("gapx 20, aligny center"));

            add(getIconLabel(MagicIcon.LEGAL, UiString.get(_S1)));
            add(getIconLabel(MagicIcon.ILLEGAL, UiString.get(_S2)));
            add(getIconLabel(MagicIcon.BANNED, UiString.get(_S3)));
            add(getIconLabel(MagicIcon.RESTRICTED, UiString.get(_S4), UiString.get(_S5)));
            add(getIconLabel(MagicIcon.RESTRICTED, UiString.get(_S6),
                    String.format("<html><b>%s</b><br>%s<br><i>Relentless Rats</i> %s <i>Shadowborn Apostle</i>.</html>",
                            UiString.get(_S7),
                            UiString.get(_S8),
                            UiString.get(_S9))
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

}
