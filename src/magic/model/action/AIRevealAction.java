package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

public class AIRevealAction extends MagicAction {

    private final List<MagicCard> cards = new ArrayList<>();
    private final List<Boolean> known = new ArrayList<>();
    private final boolean newValue;

    public AIRevealAction(final MagicCard... aCards) {
        this(Arrays.asList(aCards), true);
    }

    public AIRevealAction(final Collection<MagicCard> aCards) {
        this(aCards, true);
    }

    public static AIRevealAction Hide(final Collection<MagicCard> aCards) {
        return new AIRevealAction(aCards, false);
    }

    private AIRevealAction(final Collection<MagicCard> aCards, final boolean aNewValue) {
        cards.addAll(aCards);
        newValue = aNewValue;
    }

    @Override
    public void doAction(final MagicGame game) {
        for (final MagicCard card : cards) {
            known.add(card.isGameKnown());
            card.setGameKnown(newValue);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setGameKnown(known.get(i));
        }
    }
}
