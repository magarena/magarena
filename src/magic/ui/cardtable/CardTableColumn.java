package magic.ui.cardtable;

enum CardTableColumn {

    Rating("#", 40),
    CardName("Name", 180),
    Cost("CC", 140),
    Power("P", 30),
    Toughness("T", 30),
    Type("Type", 140),
    Subtype("Subtype", 140),
    Rarity("Rarity", 90),
    Oracle("Text", 2000);

    private final String caption;
    private final int minWidth;
    private boolean isSortDesc;

    private CardTableColumn(String aCaption, int aWidth) {
        this.caption = aCaption;
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
