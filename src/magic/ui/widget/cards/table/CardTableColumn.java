package magic.ui.widget.cards.table;

import magic.translate.UiString;

enum CardTableColumn {

    Rating("#", 40),
    CardName(EnumStrings._S1, 180),
    Cost(EnumStrings._S2, 140),
    Power(EnumStrings._S3, 30),
    Toughness(EnumStrings._S4, 30),
    Type(EnumStrings._S5, 140),
    Subtype(EnumStrings._S6, 140),
    Rarity(EnumStrings._S7, 90),
    Oracle(EnumStrings._S8, 2000);

    private final String caption;
    private final int minWidth;
    private boolean isSortDesc;

    private CardTableColumn(String aCaption, int aWidth) {
        this.caption = UiString.get(aCaption);
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
    boolean isSortDesc() {
        return isSortDesc;
    }

    void setSortDesc(boolean b) {
        this.isSortDesc = b;
    }
}
