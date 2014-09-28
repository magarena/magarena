package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

public class MagicAIRevealAction extends MagicAction {
    
    private final List<MagicCard> cards = new ArrayList<MagicCard>();
    private final List<Boolean> known = new ArrayList<Boolean>();
    private boolean newValue;
    
    public MagicAIRevealAction(final MagicCard... aCards) {
        this(Arrays.asList(aCards), true);
    }
    
    public MagicAIRevealAction(final Collection<MagicCard> aCards) {
        this(aCards, true);
    }

    public static MagicAIRevealAction Hide(final Collection<MagicCard> aCards) {
        return new MagicAIRevealAction(aCards, false);
    }

    private MagicAIRevealAction(final Collection<MagicCard> aCards, final boolean aNewValue) {
        cards.addAll(aCards);
        newValue = aNewValue;
    }

    public void doAction(final MagicGame game) {
        for (final MagicCard card : cards) {
            known.add(card.isGameKnown());
            card.setGameKnown(newValue);
        }
    }

    public void undoAction(final MagicGame game) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setGameKnown(known.get(i));
        }
    }
}
