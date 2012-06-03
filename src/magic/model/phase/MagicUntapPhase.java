package magic.model.phase;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicUntapAction;

public class MagicUntapPhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicUntapPhase();
	
	private MagicUntapPhase() {
		super(MagicPhaseType.Untap);
	}
	
	public static MagicPhase getInstance() {
		return INSTANCE;
	}
	
	private static void untap(final MagicGame game) {
		final MagicPlayer player=game.getTurnPlayer();
		final boolean exhausted=player.hasState(MagicPlayerState.Exhausted);
		game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.Exhausted,false));

		for (final MagicPermanent permanent : player.getPermanents()) {
			
			if (permanent.hasState(MagicPermanentState.Summoned)) {
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.Summoned,false));
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.MustPayEchoCost,true));
			}
			if (permanent.hasState(MagicPermanentState.DoesNotUntapDuringNext)) {
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.DoesNotUntapDuringNext,false));
			} else if (permanent.isTapped() &&
					!permanent.hasAbility(game,MagicAbility.DoesNotUntap) &&
					(!exhausted ||
					!(permanent.isLand() ||
					permanent.isCreature()))) {
				game.doAction(new MagicUntapAction(permanent));
			}
		}		
	}

	@Override
	public void executeBeginStep(final MagicGame game) {
		untap(game);
		game.setStep(MagicStep.NextPhase);
	}
}
