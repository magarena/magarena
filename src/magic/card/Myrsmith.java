package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

public class Myrsmith {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
            final MagicPlayer player = permanent.getController();
            final MagicCard card = data.getCard();
            return (card.getOwner() == player &&
                    data.getCardDefinition().isArtifact()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                                "You may pay {1}.",
                                new MagicPayManaCostChoice(MagicManaCost.ONE)),
                        new Object[]{player},
                        this,
                        player + " may$ pay {1}$. If you do, put a 1/1 " +
                        "colorless Myr artifact creature token onto the battlefield."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.get("Myr1")));
            }
        }
    };
}
