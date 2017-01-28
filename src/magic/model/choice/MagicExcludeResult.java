package magic.model.choice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MurmurHash3;
import magic.model.action.ChangeStateAction;

public class MagicExcludeResult implements MagicMappable<MagicExcludeResult> {

    private static final List<MagicPermanent> NO_EXCLUDE_PERMANENTS=Collections.emptyList();

    private final List<MagicPermanent> excludePermanents;
    private final int excludeFlags;

    MagicExcludeResult(final List<MagicPermanent> excludePermanents,final int excludeFlags) {
        this.excludePermanents=excludePermanents;
        this.excludeFlags=excludeFlags;
    }

    MagicExcludeResult() {
        this(NO_EXCLUDE_PERMANENTS,0);
    }

    @Override
    public MagicExcludeResult map(final MagicGame game) {
        final List<MagicPermanent> mappedExcludePermanents=new ArrayList<MagicPermanent>();
        for (final MagicPermanent excludePermanent : excludePermanents) {
            mappedExcludePermanents.add(excludePermanent.map(game));
        }
        return new MagicExcludeResult(mappedExcludePermanents,excludeFlags);
    }

    public void exclude(final MagicGame game) {
        final int size=excludePermanents.size();
        for (int index=0,flag=1;index<size;index++,flag<<=1) {
            final MagicPermanent permanent=excludePermanents.get(index);
            final boolean excludeFromCombat=(excludeFlags&flag)==0;
            if (excludeFromCombat) {
                game.doAction(ChangeStateAction.Set(permanent,MagicPermanentState.ExcludeFromCombat));
                game.doAction(ChangeStateAction.Clear(permanent,MagicPermanentState.ExcludeManaSource));
            } else {
                game.doAction(ChangeStateAction.Clear(permanent,MagicPermanentState.ExcludeFromCombat));
                game.doAction(ChangeStateAction.Set(permanent,MagicPermanentState.ExcludeManaSource));
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder buffer=new StringBuilder();
        for (int index=0,flag=1;index<excludePermanents.size();index++,flag<<=1) {
            if (buffer.length()>0) {
                buffer.append(", ");
            }
            final boolean combat=(excludeFlags&flag)==0;
            buffer.append(excludePermanents.get(index)).append('=').append(combat?"C":"M");
        }
        return buffer.toString();
    }

    @Override
    public long getId() {
        final long[] keys = new long[excludePermanents.size() + 1];
        int idx = 0;
        for (final MagicPermanent permanent : excludePermanents) {
            keys[idx] = permanent.getStateId();
            idx++;
        }
        keys[idx] = excludeFlags;
        idx++;
        return MurmurHash3.hash(keys);
    }
}
