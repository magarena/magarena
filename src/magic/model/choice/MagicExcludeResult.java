package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicExcludeResult implements MagicMappable {

	private static final List<MagicPermanent> NO_EXCLUDE_PERMANENTS=Collections.emptyList();
	
	private final List<MagicPermanent> excludePermanents;
	private final int excludeFlags;
	
	public MagicExcludeResult(final List<MagicPermanent> excludePermanents,final int excludeFlags) {
		this.excludePermanents=excludePermanents;
		this.excludeFlags=excludeFlags;
	}
	
	public MagicExcludeResult() {
		this(NO_EXCLUDE_PERMANENTS,0);
	}
	
	@Override
	public Object map(final MagicGame game) {
		final List<MagicPermanent> mappedExcludePermanents=new ArrayList<MagicPermanent>();
		for (final MagicPermanent excludePermanent : excludePermanents) {
			mappedExcludePermanents.add((MagicPermanent)excludePermanent.map(game));
		}
		return new MagicExcludeResult(mappedExcludePermanents,excludeFlags);
	}
	
	public void exclude(final MagicGame game) {
		final int size=excludePermanents.size();
		for (int index=0,flag=1;index<size;index++,flag<<=1) {
			final MagicPermanent permanent=excludePermanents.get(index);
			final boolean combat=(excludeFlags&flag)==0;
			game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.ExcludeFromCombat,combat));
			game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.ExcludeManaSource,!combat));
		}
	}
	
	@Override
	public String toString() {
		final StringBuffer buffer=new StringBuffer();
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
        int idx = 0;
		long[] input = new long[excludePermanents.size() + 1];
		for (final MagicPermanent permanent : excludePermanents) {
            input[idx] = permanent.getId();
            idx++;
		}
        input[input.length - 1] = excludeFlags;
		return magic.MurmurHash3.hash(input);
    }
}
