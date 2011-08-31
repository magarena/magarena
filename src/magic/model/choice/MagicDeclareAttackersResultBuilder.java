package magic.model.choice;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;

public class MagicDeclareAttackersResultBuilder {

	private static final Collection<Object> EMPTY_RESULT=Collections.<Object>singletonList(new MagicDeclareAttackersResult());
	private static final int MAX_ATTACKERS[]={5,4,3,2,1,0,0,0};

	private final MagicGame game;
	private final MagicPlayer attackingPlayer;
	private final MagicPlayer defendingPlayer;
	
	public MagicDeclareAttackersResultBuilder(final MagicGame game,final MagicPlayer attackingPlayer) {
		this.game=game;
		this.attackingPlayer=attackingPlayer;
		this.defendingPlayer=game.getOpponent(attackingPlayer);
	}
	
	public Collection<Object> buildResults() {
		final MagicCombatCreatureBuilder creatureBuilder=new MagicCombatCreatureBuilder(game,attackingPlayer,defendingPlayer);
		creatureBuilder.buildBlockers();
		
        // Check if none of the attacking player's creatures can attack.
		if (!creatureBuilder.buildAttackers()) {
			return EMPTY_RESULT;
		}
		
		// Remove creatures that have zero power.
		// Remove creatures that must attack if able and add them to result.
		final SortedSet<MagicCombatCreature> attackersSet=creatureBuilder.getAttackers();
		final MagicPermanent current[]=new MagicPermanent[attackersSet.size()];
		int count=0;
		for (final Iterator<MagicCombatCreature> iterator=attackersSet.iterator();iterator.hasNext();) {
			final MagicCombatCreature attacker=iterator.next();			 
			if (attacker.hasAbility(MagicAbility.AttacksEachTurnIfAble)) {
				current[count++]=attacker.permanent;
				iterator.remove();
			} else if (attacker.power<=0) {
				iterator.remove();
			}
		}

		int maxAttackers=MAX_ATTACKERS[game.getRelativeTurn()];
		int size=attackersSet.size();

        // Single result with the required attackers only.
		if (maxAttackers==0||size==0) {
			return Collections.<Object>singletonList(new MagicDeclareAttackersResult(current,count,0,0));
		}

		// Build results.
		final Collection<Object> results=new ArrayList<Object>();
		int position=0;
		
		// Get the best remaining optional attackers.
		while (size>maxAttackers) {
			// Add option to attack with all creatures for an alpha strike.
			final MagicDeclareAttackersResult result=new MagicDeclareAttackersResult(current,count,position++,0);
			result.addCreatures(attackersSet);
			results.add(result);
			// Remove worst attacker.
			size--;
			attackersSet.remove(attackersSet.first());
		}

		final MagicCombatCreature attackers[]=new MagicCombatCreature[size];
		attackersSet.toArray(attackers);
		
		// Build all possible combinations of attackers.
		final int step[]=new int[size];
		int index=0;
		while (index>=0) {
			if (index==size) {
				results.add(new MagicDeclareAttackersResult(current,count,position++,0));
				index--;
				continue;
			}
			switch (step[index]++) {
				case 0:
					current[count++]=attackers[index++].permanent;
					break;
				case 1:
					count--;
					index++;
					break;
				case 2:
					step[index--]=0;
					break;
			}
		}
		return results;
	}
}
