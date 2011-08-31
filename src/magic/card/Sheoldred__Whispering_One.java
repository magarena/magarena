package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Sheoldred__Whispering_One {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			return (player != data) ?
                null :
				new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{player},
                    this,
                    "Return target creature card$ from your graveyard to the battlefield.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
                }
            });
		}
    };
    
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			return (player == data) ? 
                null :
				new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent,data},
                    this,
                    "Your opponent sacrifices a creature.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer opponent=(MagicPlayer)data[1];
			if (opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(
                            (MagicPermanent)data[0],
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
    };
}
