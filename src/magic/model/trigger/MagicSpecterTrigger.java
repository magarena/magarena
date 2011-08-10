package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;

public class MagicSpecterTrigger extends MagicTrigger {

	private final boolean combat;
	private final boolean random;
	
	public MagicSpecterTrigger(final String name,final boolean combat,final boolean random) {
		
		super(MagicTriggerType.WhenDamageIsDealt,name);
		this.combat=combat;
		this.random=random;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

		final MagicDamage damage=(MagicDamage)data;
		final MagicTarget target=damage.getTarget();
		if (damage.getSource()==permanent&&target.isPlayer()&&(!combat||damage.isCombat())) {
			final MagicPlayer player=(MagicPlayer)target;
			final String playerName = player.getName();
			final String opponentName = permanent.getController().getName();
			String prefix = player == permanent.getController() ? opponentName : playerName;
			prefix += " discards a card";
			return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,player},this,random?prefix+" at random.":prefix+'.');
		}
		return null;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choices) {

		final MagicPermanent permanent=(MagicPermanent)data[0];
		final MagicPlayer player=(MagicPlayer)data[1];
		game.addEvent(new MagicDiscardEvent(permanent,player,1,random));
	}
}