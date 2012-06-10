package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Conjurer_s_Closet {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            "You may exile target creature you control.",
                            MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL),
                    MagicBounceTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "You may$ exile target creature$ you control, then return " +
                    "that card to the battlefield under your control."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
					public void doAction(final MagicPermanent creature) {
						game.doAction(new MagicRemoveFromPlayAction(
								creature,
								MagicLocationType.Exile));
						game.doAction(new MagicRemoveCardAction(
								creature.getCard(),
								MagicLocationType.Exile));
						game.doAction(new MagicPlayCardAction(
								creature.getCard(),
								event.getPlayer(),
								MagicPlayCardAction.NONE));
					}
				});
			}
		}
    };
}
