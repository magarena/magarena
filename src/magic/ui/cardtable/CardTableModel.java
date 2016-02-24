package magic.ui.cardtable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicManaCost;

public class CardTableModel implements TableModel {

    private static final NumberFormat ratingFormatter = new DecimalFormat("#0.0");

    static final int[] COLUMN_MIN_WIDTHS = {
                                                40,    // 0 #
                                                180,   // 1 name
                                                140,   // 2 cc
                                                30,    // 3 P
                                                30,    // 4 T
                                                140,   // 5 type
                                                140,   // 6 subtype
                                                90,    // 7 rarity
                                                2000}; // 8 text

    public static final int COST_COLUMN_INDEX = 2;

    private boolean showCardCount = false;

    private boolean[] isDesc = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    private MagicCondensedDeck cardDefinitions;
    private Comparator<MagicCondensedCardDefinition> comp;

    public CardTableModel(final List<MagicCardDefinition> cardDefs) {
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

    public void sort(final int column) {
        final Comparator<MagicCondensedCardDefinition> oldComp = comp;
        comp = null;

        switch(column) {
            case 0:
                if (showCardCount) {
                    comp = isDesc[column] ? MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_ASC : MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_DESC;
                } else {
                    comp = isDesc[column] ? MagicCondensedCardDefinition.RATING_COMPARATOR_ASC : MagicCondensedCardDefinition.RATING_COMPARATOR_DESC;
                }
                        break;
            case 1:        comp = isDesc[column] ? MagicCondensedCardDefinition.NAME_COMPARATOR_ASC : MagicCondensedCardDefinition.NAME_COMPARATOR_DESC;
                        break;
            case 2:        comp = isDesc[column] ? MagicCondensedCardDefinition.CONVERTED_COMPARATOR_ASC : MagicCondensedCardDefinition.CONVERTED_COMPARATOR_DESC;
                        break;
            case 3:        comp = isDesc[column] ? MagicCondensedCardDefinition.POWER_COMPARATOR_ASC : MagicCondensedCardDefinition.POWER_COMPARATOR_DESC;
                        break;
            case 4:        comp = isDesc[column] ? MagicCondensedCardDefinition.TOUGHNESS_COMPARATOR_ASC : MagicCondensedCardDefinition.TOUGHNESS_COMPARATOR_DESC;
                        break;
            case 5:        comp = isDesc[column] ? MagicCondensedCardDefinition.TYPE_COMPARATOR_ASC : MagicCondensedCardDefinition.TYPE_COMPARATOR_DESC;
                        break;
            case 6:        comp = isDesc[column] ? MagicCondensedCardDefinition.SUBTYPE_COMPARATOR_ASC : MagicCondensedCardDefinition.SUBTYPE_COMPARATOR_DESC;
                        break;
            case 7:        comp = isDesc[column] ? MagicCondensedCardDefinition.RARITY_COMPARATOR_ASC : MagicCondensedCardDefinition.RARITY_COMPARATOR_DESC;
                        break;
        }

        if (comp != null) {
            // new sort
            Collections.sort(cardDefinitions, comp);
            isDesc[column] = !isDesc[column];
        } else {
            // didn't select valid new sort -> reset to old
            comp = oldComp;
        }
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex)
    {
        switch (columnIndex) {
            case 1:        return MagicManaCost.class;
        }
        return String.class;
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
    public int getRowCount()
    {
        return cardDefinitions.size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final MagicCardDefinition card = cardDefinitions.get(rowIndex).getCard();

        switch (columnIndex) {
            case 0:
                if (showCardCount) {
                    return Integer.toString(cardDefinitions.get(rowIndex).getNumCopies());
                } else {
                    return ratingFormatter.format(card.getValue());
                }
            case 1:
                if (card.isSplitCard()) {
                    if (card.isSecondHalf()) {
                        return card.getSplitDefinition().getName() + " // " + card.getName() + " (" + card.getName() + ")";
                    } else {
                        return card.getName() + " // " + card.getSplitDefinition().getName() + " (" + card.getName() + ")";
                    }
                } else {
                    return card.getName();
                }
            case 2:
                return card.getCost();
            case 3:
                if (card.isCreature()) {
                    return card.getCardPower();
                } else {
                    return "";
                }
            case 4:
                if (card.isCreature()) {
                    return card.getCardToughness();
                } else {
                    return "";
                }
            case 5:
                return card.getLongTypeString();
            case 6:
                return card.getSubTypeString();
            case 7:
                return card.getRarityString();
            case 8:
                return card.getFlattenedText();
        }

        return "";
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        // do nothing.
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        // do nothing.
    }
}

