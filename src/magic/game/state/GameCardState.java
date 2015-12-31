package magic.game.state;

public class GameCardState {

    private final String cardName;
    private final int quantity;
    private final boolean isTapped;

    GameCardState(String cardName, int quantity, boolean tapped) {
        this.cardName = cardName;
        this.quantity = quantity;
        this.isTapped = tapped;
    }
    GameCardState(String cardName, int quantity) {
        this(cardName, quantity, false);
    }

    public String getCardName() {
        return cardName;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isTapped() {
        return isTapped;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof GameCardState)) {
            return false;
        } else {
            final GameCardState other = (GameCardState)obj;
            return other.cardName.equals(getCardName()) &&
                   other.isTapped() == isTapped();
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (cardName == null ? 0 : cardName.hashCode());
        hash = 37 * hash + (isTapped ? 1 : 0);
        return hash;
    }

}
