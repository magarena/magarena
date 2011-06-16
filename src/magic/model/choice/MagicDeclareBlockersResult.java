package magic.model.choice;

import java.util.Arrays;
import java.util.LinkedList;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.score.MagicScoreResult;

/** First creature in array is the attacker, the other creatures are blockers. */
public class MagicDeclareBlockersResult extends LinkedList<MagicCombatCreature[]> implements MagicMappable,MagicScoreResult {

	private static final long serialVersionUID = 1L;

	private final int position;
	private final int score;

	public MagicDeclareBlockersResult(final int position,final int score) {
		this.position=position;
		this.score=score;		
	}
	
	public MagicDeclareBlockersResult(final MagicDeclareBlockersResult result,final int position,final int score) {
		this(position,score);
		for (final MagicCombatCreature creatures[] : result) {
			add(Arrays.copyOf(creatures,creatures.length));
		}
	}

	@Override
	public Object map(final MagicGame game) {
		final MagicDeclareBlockersResult result=new MagicDeclareBlockersResult(position,score);
		for (final MagicCombatCreature creatures[] : this) {

			final int size=creatures.length;
			final MagicCombatCreature mappedCreatures[]=new MagicCombatCreature[size];
			for (int index=size-1;index>=0;index--) {
				
				mappedCreatures[index]=new MagicCombatCreature(game,creatures[index]);
			}
			result.add(mappedCreatures);
		}
		return result;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public int getScore() {
		return score;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder=new StringBuilder();
		//builder.append(score);
		for (final MagicCombatCreature creatures[] : this) {
			if (creatures.length>1) {
				builder.append(' ');
                builder.append(creatures[0].getName());
                builder.append('=');
                builder.append(creatures[1].getName());
                builder.append(creatures.length > 2 ? "+" + (creatures.length-2) : "");
			}
		}
		return builder.toString();
	}
}
