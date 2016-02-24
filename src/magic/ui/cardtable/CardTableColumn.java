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

    private CardTableColumn(String aCaption) {
        this.caption = aCaption;
    }

    String getCaption() {
        return caption;
    }
}
