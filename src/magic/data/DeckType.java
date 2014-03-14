package magic.data;

public enum DeckType {

    Random("Random"),
    Preconstructed("Preconstructed"),
    Custom("Custom")
    ;

    private final String deckTypeCaption;

    private DeckType(final String caption) {
        this.deckTypeCaption = caption;
    }

    @Override
    public String toString() {
        return deckTypeCaption;
    }

}
