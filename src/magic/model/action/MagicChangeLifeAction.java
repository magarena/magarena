package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

/** Keeping the player life is done in the marker action. */
public class MagicChangeLifeAction extends MagicAction {
	
	private final MagicPlayer player;
	private final int life;
    private final boolean isDamage;
	
	public MagicChangeLifeAction(final MagicPlayer aPlayer,final int aLife, final boolean aIsDamage) {
		player = aPlayer;
		life = aLife;
        isDamage = aIsDamage;

	}
	
    public MagicChangeLifeAction(final MagicPlayer aPlayer,final int aLife) {
        this(aPlayer,aLife,false);
	}
	
	@Override
	public void doAction(final MagicGame game) {
		final int oldLife = player.getLife();
		final int newLife = oldLife+life;
        
		player.setLife(newLife);
        if (isDamage) {
            game.executeTrigger(MagicTriggerType.WhenLifeIsDamaged,player);
        }
        final int actualLife = player.getLife();
       
		setScore(player,ArtificialScoringSystem.getLifeScore(actualLife)-ArtificialScoringSystem.getLifeScore(oldLife));
		if (actualLife > oldLife) {
			game.executeTrigger(MagicTriggerType.WhenLifeIsGained,player);
		}
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" ("+player.getName()+','+life+')';
	}
}
