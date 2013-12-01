package magic.ui;

public interface IDeckMenuConsumer {

    public enum DeckMenuItem {
        ALL(""),
        NEW_DECK("New Deck"),
        LOAD_DECK("Load Deck"),
        SAVE_DECK("Save Deck"),
        SWAP_DECKS("Swap Decks"),;
        private final String menuCaption;
        private DeckMenuItem(final String menuCaption0) { this.menuCaption = menuCaption0; }
        @Override public String toString() { return this.menuCaption; }
    }

    boolean isDeckMenuItemVisible(DeckMenuItem menu);

    boolean isDeckMenuItemEnabled(DeckMenuItem menu);

}
