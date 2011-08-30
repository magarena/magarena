package magic.card;

import magic.model.*;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.action.MagicPermanentAction;

public class Diminish {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE,
                    new MagicDestroyTargetPicker(true),
                    new Object[]{cardOnStack},
                    this,
                    "Target creature$ becomes 1/1 until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicBecomesCreatureAction(creature,
                        new MagicDummyLocalVariable() {
                            @Override
                            public void getPowerToughness(
                                final MagicGame game,
                                final MagicPermanent permanent,
                                final MagicPowerToughness pt) {
                                pt.power=1;
                                pt.toughness=1;
                            }
                        }
                    ));
                }
			});
		}
	};
}
