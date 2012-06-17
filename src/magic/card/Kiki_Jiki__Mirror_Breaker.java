package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicCopyTargetPicker;

public class Kiki_Jiki__Mirror_Breaker {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Copy") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player=source.getController();
            return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL,
                    MagicCopyTargetPicker.create(),
                    new Object[]{player},
                    this,
                    "Put a token that's a copy of target nonlegendary creature$ you control onto the battlefield. "+
                    "That token has haste. Sacrifice it at end of turn.");
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayer player=(MagicPlayer)data[0];
                    final MagicCard card=MagicCard.createTokenCard(creature.getCardDefinition(),player);
                    game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.HASTE_SACRIFICE_AT_END_OF_TURN));
                }
            });
        }
    };
}
