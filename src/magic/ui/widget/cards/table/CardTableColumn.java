package magic.ui.widget.cards.table;

import magic.translate.MText;
import magic.translate.StringContext;

enum CardTableColumn {

    Rating("#", 40),
    CardName(ColumnHeading._S1, 180),
    Cost(ColumnHeading._S2, 140),
    Power(ColumnHeading._S3, 30),
    Toughness(ColumnHeading._S4, 30),
    Type(ColumnHeading._S5, 140),
    Subtype(ColumnHeading._S6, 140),
    Rarity(ColumnHeading._S7, 90),
    Oracle(ColumnHeading._S8, 2000);

    private final String caption;
    private final int minWidth;

    private CardTableColumn(String aCaption, int aWidth) {
        this.caption = MText.get(aCaption);
        this.minWidth = aWidth;
    }

    String getCaption() {
        return caption;
    }

    int getMinWidth() {
        return minWidth;
    }

    static int getMinWidth(int i) {
        return values()[i].getMinWidth();
    }
}

class ColumnHeading {
    private ColumnHeading() {}

    static final String _S1 = "Name";

    @StringContext(eg = "Short hand for Combined Cost, a column header in card table.")
    static final String _S2 = "CC";

    @StringContext(eg = "Short hand for Power, a column header in card table.")
    static final String _S3 = MText.abbreviate("P", "Power");

    @StringContext(eg = "Short hand for Toughness, a column header in card table.")
    static final String _S4 = MText.abbreviate("T", "Toughness");

    static final String _S5 = "Type";
    static final String _S6 = "Subtype";
    static final String _S7 = "Rarity";
    static final String _S8 = "Text";
}

