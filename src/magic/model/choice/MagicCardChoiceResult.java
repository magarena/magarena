package magic.model.choice;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MurmurHash3;

@SuppressWarnings("serial")
public class MagicCardChoiceResult extends ArrayList<MagicCard> implements MagicMappable<MagicCardChoiceResult> {

    MagicCardChoiceResult(final MagicCard[] cards) {
        for (final MagicCard card : cards) {
            if (card != null) {
                add(card);
            }
        }
    }

    MagicCardChoiceResult() {}

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
            buffer.append(card.getName());
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
        return MurmurHash3.hash(keys);
    }
}
