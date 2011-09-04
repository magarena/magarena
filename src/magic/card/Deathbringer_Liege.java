package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTapTargetPicker;
import magic.model.trigger.MagicWhenSpellIsPlayedTrigger;


public class Deathbringer_Liege {
    public static final MagicWhenSpellIsPlayedTrigger T = new MagicWhenSpellIsPlayedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=data.getCard();
			return (card.getOwner()==player&&MagicColor.Black.hasColor(card.getColorFlags())) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may destroy target creature if it's tapped.",
                            MagicTargetChoice.NEG_TARGET_CREATURE),
                        new MagicDestroyTargetPicker(false),
                        MagicEvent.NO_DATA,
                        this,
                        "You may$ destroy target creature$ if it's tapped."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        if (creature.isTapped()) {					
                            game.doAction(new MagicDestroyAction(creature));
                        }
                    }
                });
            }
		}
    };
    
    public static final MagicWhenSpellIsPlayedTrigger T2 = new MagicWhenSpellIsPlayedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=data.getCard();
			return (card.getOwner()==player&&MagicColor.White.hasColor(card.getColorFlags())) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice("You may tap target creature.",MagicTargetChoice.NEG_TARGET_CREATURE),
                        new MagicTapTargetPicker(true,false),
                        MagicEvent.NO_DATA,
                        this,
                        "You may$ tap target creature$."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        if (creature.isTapped()) {
                            game.doAction(new MagicTapAction(creature,true));
                        }
                    }
                });
			}
		}
    };
}
