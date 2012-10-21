package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Driver_of_the_Dead {
    private static final MagicTargetFilter<MagicCard> targetFilter = new MagicTargetFilter.MagicCMCCardFilter(
        MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
        MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
        2
    );
    private static final MagicTargetChoice targetChoice = new MagicTargetChoice(
        targetFilter,false,MagicTargetHint.None,
        "target creature card from your graveyard)"
    );
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    new MagicGraveyardTargetPicker(false),
                    this,
                    "PN may$ return target creature card$ with " +
                    "converted mana cost 2 or less " +
                    "from his or her graveyard to the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction(
                                event.getPlayer(),
                                card,
                                MagicPlayCardAction.NONE));
                    }
                });
            }
        }
    };
}
