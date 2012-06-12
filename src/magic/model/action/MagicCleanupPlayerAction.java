package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;

public class MagicCleanupPlayerAction extends MagicAction {

	private final MagicPlayer player;
	private int oldStateFlags;
	private int oldPreventDamage;
	private int oldDrawnCards;
	
	public MagicCleanupPlayerAction(final MagicPlayer player) {
		this.player=player;
	}

	@Override
	public void doAction(final MagicGame game) {
		oldStateFlags=player.getStateFlags();
		player.setStateFlags(oldStateFlags&MagicPlayerState.CLEANUP_MASK);

        oldPreventDamage=player.getPreventDamage();
		player.setPreventDamage(0);

        oldDrawnCards=player.getDrawnCards();
        player.setDrawnCards(0);

		for (final MagicPermanent permanent : player.getPermanents()) {
			if (permanent.isCreature()) {
				game.doAction(new MagicCleanupCreatureAction(permanent));
			} else {
				game.doAction(new MagicCleanupPermanentAction(permanent));
			}
		}
		
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		player.setStateFlags(oldStateFlags);
		player.setPreventDamage(oldPreventDamage);
        player.setDrawnCards(oldDrawnCards);
		game.setStateCheckRequired();
	}
}
