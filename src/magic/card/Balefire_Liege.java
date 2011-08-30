package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPlayerAction;

public class Balefire_Liege {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			return (card.getOwner()==player&&MagicColor.Red.hasColor(card.getColorFlags())) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.NEG_TARGET_PLAYER,
                        new Object[]{permanent},
                        this,
                        permanent + " deals 3 damage to target player$."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],player,3,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}		
    };
    
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			return (card.getOwner()==player&&MagicColor.White.hasColor(card.getColorFlags())) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " gains 3 life."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };
}
