package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Archon_of_Redemption {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,permanent},
                    this,
                    player.getName() + " gain life equal to " + permanent.getName() + "'s power.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower(game)));
		}		
    };

    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			return (otherPermanent!=permanent && 
                    otherPermanent.getController()==player && 
                    otherPermanent.isCreature() &&
                    otherPermanent.hasAbility(game,MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,otherPermanent},
                        this,
                        player.getName() + " gain life equal to the power of "+otherPermanent.getName()+'.'):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower(game)));
		}		
    };
}
