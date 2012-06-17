package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Mirrorworks {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    !otherPermanent.getCard().isToken() &&
                    otherPermanent.isArtifact()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                                "You may pay {2}.",
                                new MagicPayManaCostChoice(MagicManaCost.TWO)),
                            new Object[]{otherPermanent.getCardDefinition()},
                        this,
                        "You may$ pay {2}. If you do, put a token that's a " +
                        "copy of " + otherPermanent + " onto the battlefield."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicCard card = MagicCard.createTokenCard(
                        (MagicCardDefinition)data[0],
                        event.getPlayer());
                game.doAction(new MagicPlayCardAction(
                        card,
                        event.getPlayer(),
                        MagicPlayCardAction.NONE));
            }            
        }        
    };
}
