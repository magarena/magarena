package magic.model.phase;

import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicCombatDamageAction;
import magic.model.action.MagicStackResolveAction;
import magic.model.event.MagicPriorityEvent;

public class MagicCombatDamagePhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicCombatDamagePhase();
    private boolean regular = false;

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
        
        //regular == false -> do first strike damage
        //regular == true  -> do regular damage
  		game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer, !regular));

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

		if (!regular) {
            regular = true;
            game.setStep(MagicStep.ActivePlayer);
        } else {
            regular = false;
            game.setStep(MagicStep.NextPhase);
		    SoundEffects.getInstance().playClip(game,SoundEffects.COMBAT_SOUND);
        }
	}	
	
    @Override
    public void executePhase(final MagicGame game) {

		switch (game.getStep()) {
			case Begin:
				executeBeginStep(game);
				break;
			case ActivePlayer:
				game.addEvent(new MagicPriorityEvent(game.getPlayer(0)));
				break;
			case OtherPlayer:
				game.addEvent(new MagicPriorityEvent(game.getOpponent(game.getTurnPlayer())));
				break;
			case Resolve:
				// Stack can be empty at this point, for instance by a counter unless event.
				if (!game.getStack().isEmpty()) {
					game.doAction(new MagicStackResolveAction());
					SoundEffects.getInstance().playClip(game,SoundEffects.RESOLVE_SOUND);
				}
                if (game.isArtificial()) {
					// Resolve stack in one go.
					if (game.getStack().isEmpty()) {
                        game.setStep(MagicStep.NextPhase);
					}
                } else {
                    game.setStep(MagicStep.ActivePlayer);						
                }
				break;
			case NextPhase:
				executeEndOfPhase(game);
				game.changePhase(game.getGameplay().getNextPhase(game));
				break;
		}
		game.checkState();
	}
}
