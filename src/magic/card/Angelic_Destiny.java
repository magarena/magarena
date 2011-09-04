package magic.card;

import java.util.EnumSet;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;
import magic.model.variable.MagicDummyLocalVariable;

public class Angelic_Destiny {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.getInstance()){
		
		@Override
		public void executeEvent(
	            final MagicGame game,
	            final MagicEvent event,
	            final Object[] data,
	            final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
	        final boolean success = event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
	            public void doAction(final MagicPermanent creature) {
	                game.doAction(new MagicPlayCardFromStackAction(cardOnStack,creature));
	                creature.addLocalVariable(
	                	new MagicDummyLocalVariable() {
	                		@Override
	                		public EnumSet<MagicSubType> getSubTypeFlags(
	                				final MagicPermanent permanent,
	                				final EnumSet<MagicSubType> flags) {
	                			final EnumSet<MagicSubType> mod = flags.clone();
	                            mod.add(MagicSubType.Angel);
	                			return mod;
	                		}
	                	}
	                );
	            }
	        });
			if (!success) {
				game.doAction(new MagicMoveCardAction(cardOnStack));
			}
		}
	};

	public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			return (permanent.getEnchantedCreature() == data) ?
				new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent.getCard()},
                    this,
                    "Return " + permanent + " to its owner's hand."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard card = (MagicCard)data[0];
			if (card.getOwner().getGraveyard().contains(card)) {
				game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
				game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
			}
		}
    };
}
