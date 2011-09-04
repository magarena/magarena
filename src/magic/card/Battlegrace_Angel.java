package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Battlegrace_Angel {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player=permanent.getController();
			return (creature.getController()==player&&player.getNrOfAttackers()==1)?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{creature},
                        this,
                        creature+" gains lifelink until end of turn."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {			
			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.LifeLink));
		}
    };
}
