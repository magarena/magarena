package magic.model.phase;

import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicCombatDamageAction;
import magic.model.action.MagicStackResolveAction;
import magic.model.event.MagicPriorityEvent;

public class MagicCombatDamagePhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicCombatDamagePhase();

	private MagicCombatDamagePhase() {
		super(MagicPhaseType.CombatDamage);
	}

	public static MagicPhase getInstance() {
		return INSTANCE;
	}

	@Override
	public void executeBeginStep(final MagicGame game) {
		final MagicPlayer attackingPlayer=game.getTurnPlayer();
		final MagicPlayer defendingPlayer=game.getOpponent(attackingPlayer);
		final int lifeBefore=defendingPlayer.getLife();
		final int poisonBefore=defendingPlayer.getPoison();
        
  		//deal first strike damage
        if (game.getStep() == MagicStep.Begin) {
            game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer, true));
        } else {
        //deal regular damage
            game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer, false));
        }

		final int lifeAfter=defendingPlayer.getLife();
		final int poisonAfter=defendingPlayer.getPoison();
		final StringBuffer message=new StringBuffer();
		if (lifeAfter>lifeBefore) {
			message.append(" You gain "+(lifeAfter-lifeBefore)+" life.");
		} else if (lifeAfter<lifeBefore) {
			message.append(" You lose "+(lifeBefore-lifeAfter)+" life.");
		}
		if (poisonAfter>poisonBefore) {
			message.append(" You get "+(poisonAfter-poisonBefore)+" poison counters.");
		}
		if (message.length()>0) {
			game.logMessage(defendingPlayer,"{c}"+message.toString());			
		}

        if (game.getStep() == MagicStep.Begin) {
            game.setStep(MagicStep.ActivePlayer);
        } else {
            SoundEffects.getInstance().playClip(game,SoundEffects.COMBAT_SOUND);
        }
	}	
	
    protected void executeEndOfPhase(final MagicGame game) {
        executeBeginStep(game);
    }
}
