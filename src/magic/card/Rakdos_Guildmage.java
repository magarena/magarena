package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicWeakenTargetPicker;

public class Rakdos_Guildmage {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.HAS_CARD_CONDITION,
                MagicManaCost.THREE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal,true),
            "-2/-2") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_BLACK),
				new MagicDiscardEvent(source,source.getController(),1,false)
			};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(2,2),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets -2/-2 until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,-2,-2));
                }
			});
		}
	};

	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.THREE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_RED)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put a 2/1 red Goblin creature token with haste onto the battlefield. Exile it at end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.GOBLIN2_TOKEN_CARD,player);
			game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.REMOVE_AT_END_OF_TURN));
		}
	};
}
