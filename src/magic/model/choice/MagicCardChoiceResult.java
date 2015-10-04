package magic.model.choice;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicCopyable;
import magic.model.MagicCopyMap;

import java.util.List;
import java.util.ArrayList;
import magic.model.MagicMessage;

public class MagicCardChoiceResult extends ArrayList<MagicCard> implements MagicMappable<MagicCardChoiceResult>, MagicCopyable {

    private static final long serialVersionUID = 1L;

    MagicCardChoiceResult(final MagicCard[] cards) {
        for (final MagicCard card : cards) {
            if (card != null) {
                add(card);
            }
        }
    }

    MagicCardChoiceResult() {}
    
    private MagicCardChoiceResult(final MagicCopyMap copyMap, final List<MagicCard> cardList) {
        for (final MagicCard card : cardList) {
            add(copyMap.copy(card));
        }
    }
    
    @Override
    public MagicCardChoiceResult copy(final MagicCopyMap copyMap) {
        return new MagicCardChoiceResult(copyMap, this);
    }


    @Override
    public MagicCardChoiceResult map(final MagicGame game) {
        final MagicCardChoiceResult result=new MagicCardChoiceResult();
        for (final MagicCard card : this) {
            result.add(card.map(game));
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer=new StringBuilder();
        for (final MagicCard card : this)  {
            if (buffer.length()>0) {
                buffer.append(", ");
            }
            buffer.append(MagicMessage.getCardToken(card));
        }
        return buffer.toString();
    }

    @Override
    public long getId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicCard card : this) {
            keys[idx] = card.getStateId();
            idx++;
        }
        return magic.model.MurmurHash3.hash(keys);
    }
}
