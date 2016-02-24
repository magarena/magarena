package magic.ui.cardtable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicManaCost;

@SuppressWarnings("serial")
public class DeckTableModel extends AbstractTableModel {

    private static final NumberFormat RATING_FMT = new DecimalFormat("#0.0");

    private boolean showCardCount = false;
    private MagicCondensedDeck cardDefinitions;
    private Comparator<MagicCondensedCardDefinition> comp;

    public DeckTableModel(final List<MagicCardDefinition> cardDefs) {
        this.comp = MagicCondensedCardDefinition.NAME_COMPARATOR_DESC;
        setCards(cardDefs);
    }

    public void showCardCount(final boolean b) {
        this.showCardCount = b;
    }

    public MagicCardDefinition getCardDef(final int row) {
        if (row < 0 || row >= cardDefinitions.size()) {
            return null;
        }
        return cardDefinitions.get(row).getCard();
    }

    public int findCardIndex(final MagicCardDefinition card) {
        for (int i = 0; i < cardDefinitions.size(); i++) {
            if (MagicCardDefinition.NAME_COMPARATOR_ASC.compare(cardDefinitions.get(i).getCard(), card) == 0) {
                return i;
            }
        }
        return -1;
    }

    public void setCards(final List<MagicCardDefinition> defs) {
        this.cardDefinitions = new MagicCondensedDeck(defs);

        // re-sort if necessary
        if (comp != null) {
            Collections.sort(cardDefinitions, comp);
        }
    }

    private Comparator<MagicCondensedCardDefinition> getSortComparator(final CardTableColumn col) {
        switch(col) {
            case Rating:
                return showCardCount
                    ? col.isSortDesc()
                        ? MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_ASC
                        : MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_DESC
                    : col.isSortDesc()
                        ? MagicCondensedCardDefinition.RATING_COMPARATOR_ASC
                        : MagicCondensedCardDefinition.RATING_COMPARATOR_DESC;

            case CardName:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.NAME_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.NAME_COMPARATOR_DESC;

            case Cost:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.CONVERTED_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.CONVERTED_COMPARATOR_DESC;

            case Power:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.POWER_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.POWER_COMPARATOR_DESC;

            case Toughness:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.TOUGHNESS_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.TOUGHNESS_COMPARATOR_DESC;

            case Type:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.TYPE_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.TYPE_COMPARATOR_DESC;
                
            case Subtype:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.SUBTYPE_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.SUBTYPE_COMPARATOR_DESC;

            case Rarity:
                return col.isSortDesc()
                    ? MagicCondensedCardDefinition.RARITY_COMPARATOR_ASC
                    : MagicCondensedCardDefinition.RARITY_COMPARATOR_DESC;

            default:
                return null;
        }
    }

    public void sort(final int column) {
        final CardTableColumn col = CardTableColumn.values()[column];
        final Comparator<MagicCondensedCardDefinition> oldComp = comp;
        comp = getSortComparator(col);
        if (comp != null) {
            // new sort
            Collections.sort(cardDefinitions, comp);
            col.setSortDesc(!col.isSortDesc());
        } else {
            // didn't select valid new sort -> reset to old
            comp = oldComp;
        }
    }

    @Override
    public Class<?> getColumnClass(final int col) {
        return col == 1 ? MagicManaCost.class : String.class;
    }

    @Override
    public int getColumnCount() {
        return CardTableColumn.values().length;
    }

    @Override
    public String getColumnName(final int col) {
        return CardTableColumn.values()[col].getCaption();
    }

    @Override
    public int getRowCount() {
        return cardDefinitions.size();
    }

    private String getCardNameValue(MagicCardDefinition card ) {
        if (card.isSplitCard()) {
            if (card.isSecondHalf()) {
                return card.getSplitDefinition().getName() + " // " + card.getName() + " (" + card.getName() + ")";
            } else {
                return card.getName() + " // " + card.getSplitDefinition().getName() + " (" + card.getName() + ")";
            }
        } else {
            return card.getName();
        }
    }
    
    @Override
    public Object getValueAt(final int row, final int col) {

        final MagicCondensedCardDefinition ccard = cardDefinitions.get(row);
        final MagicCardDefinition card = ccard.getCard();

        switch(CardTableColumn.values()[col]) {
            case Rating:
                return showCardCount
                    ? Integer.toString(ccard.getNumCopies())
                    : RATING_FMT.format(card.getValue());
            case CardName:
                return getCardNameValue(card);
            case Cost:
                return card.getCost();
            case Power:
                return card.isCreature() ? card.getCardPower() : "";
            case Toughness:
                return card.isCreature() ? card.getCardToughness() : "";
            case Type:
                return card.getLongTypeString();
            case Subtype:
                return card.getSubTypeString();
            case Rarity:
                return card.getRarityString();
            case Oracle:
                return card.getFlattenedText();
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

}

