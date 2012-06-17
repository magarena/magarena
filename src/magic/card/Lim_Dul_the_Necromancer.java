package magic.card;

import java.util.EnumSet;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicRegenerateTargetPicker;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Lim_Dul_the_Necromancer {
	private static final MagicStatic Zombie = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            flags.add(MagicSubType.Zombie);
        }
   };

   public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
		    final MagicPlayer player = permanent.getController();	
			final MagicPlayer otherController = otherPermanent.getController();
			return (otherController != player &&
					otherPermanent.isCreature()) ?
				new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                        		"You may pay {1}{B}.",
                        		new MagicPayManaCostChoice(MagicManaCost.ONE_BLACK)),
                        new Object[]{player,otherPermanent.getCard()},
                        this,
                        "You may$ pay {1}{B}$. If you do, return " + otherPermanent + 
                        " to the battlefield under your control. If it's a " +
                        "creature, it's a Zombie in addition to its other creature types."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card = (MagicCard)data[1];
				if (card.getOwner().getGraveyard().contains(card)) {
					final MagicPlayCardAction action = new MagicPlayCardAction(card,(MagicPlayer)data[0],MagicPlayCardAction.NONE);
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					game.doAction(action);
					final MagicPermanent permanent = action.getPermanent();
					if (permanent.isCreature()) {
						game.doAction(new MagicAddStaticAction(permanent,Zombie));
					}
				}
			}
		}
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.ONE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Regen") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_BLACK)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_ZOMBIE,
                    MagicRegenerateTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Regenerate target Zombie$.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    };
}
