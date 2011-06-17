package magic.model.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;

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
	public Object map(final MagicGame game) {
		final MagicDeclareAttackersResult result=new MagicDeclareAttackersResult();
		for (final MagicPermanent permanent : this) {
			result.add((MagicPermanent)permanent.map(game));
		}
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}
}
