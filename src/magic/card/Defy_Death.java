package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardAction;
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
                    cardOnStack,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    this,
                    "Return target creature card$ from your graveyard to the " +
                    "battlefield. If it's an Angel, put two +1/+1 counters on it.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                    final MagicPlayCardAction action = new MagicPlayCardAction(targetCard,event.getPlayer(),MagicPlayCardAction.NONE);
                    game.doAction(action);
                    final MagicPermanent permanent = action.getPermanent();
                    if (permanent.hasSubType(MagicSubType.Angel)) {
                        game.doAction(new MagicChangeCountersAction(
                                permanent,
                                MagicCounterType.PlusOne,
                                2,
                                true));
                    }
                }
            });
        }
    };
}
