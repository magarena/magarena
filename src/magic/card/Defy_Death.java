package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicAction;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPutIntoPlayAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Defy_Death {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{cardOnStack},
                    this,
                    "Return target creature card$ from your graveyard to the " +
                    "battlefield. If it's an Angel, put two +1/+1 counters on it.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    if (targetCard.getOwner().getGraveyard().contains(targetCard)) {
                        game.doAction(new MagicRemoveCardAction(
                                targetCard,
                                MagicLocationType.Graveyard));
                        final MagicAction action = new MagicPlayCardAction(
                                targetCard,
                                targetCard.getOwner(),
                                MagicPlayCardAction.NONE);
                        game.doAction(action);
                        final MagicPermanent permanent = ((MagicPutIntoPlayAction)action).getPermanent();
                        if (permanent.hasSubType(MagicSubType.Angel)) {
                            game.doAction(new MagicChangeCountersAction(
                                    permanent,
                                    MagicCounterType.PlusOne,
                                    2,
                                    true));
                        }
                    }   
                }
            });
        }
    };
}
