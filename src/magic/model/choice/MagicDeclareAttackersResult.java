package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MagicDeclareAttackersResult extends ArrayList<MagicPermanent> implements MagicMappable {

	private static final long serialVersionUID = 1L;
	
	public MagicDeclareAttackersResult() {}

	public MagicDeclareAttackersResult(final MagicPermanent attackers[],final int length,final int position,final int score) {
		for (int index=0;index<length;index++) {
			add(attackers[index]);
		}
	}
	
	public void addCreatures(final Collection<MagicCombatCreature> creatures) {
		for (final MagicCombatCreature creature : creatures) {
			add(creature.permanent);
		}
	}

	@Override
	public MagicDeclareAttackersResult map(final MagicGame game) {
		final MagicDeclareAttackersResult result=new MagicDeclareAttackersResult();
		for (final MagicPermanent permanent : this) {
			result.add(permanent.map(game));
		}
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}

    @Override
    public long getId() {
        int idx = 0;
		final long[] input = new long[size() + 1];
		for (final MagicPermanent permanent : this) {
            input[idx] = permanent.getId();
            idx++;
		}
		return magic.MurmurHash3.hash(input);
    }
}
