package magic.ui.cardtable;

enum CardTableColumn {
    Rating("#"),
    CardName("Name"),
    Cost("CC"),
    Power("P"),
    Toughness("T"),
    Type("Type"),
    Subtype("Subtype"),
    Rarity("Rarity"),
    Oracle("Text");

    private final String caption;
    private boolean isSortDesc;

    private CardTableColumn(String aCaption) {
        this.caption = aCaption;
    }

    String getCaption() {
        return caption;
    }

    boolean isSortDesc() {
        return isSortDesc;
    }

    void setSortDesc(boolean b) {
        this.isSortDesc = b;
    }
}
