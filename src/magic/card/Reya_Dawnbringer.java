package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Reya_Dawnbringer {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player != data) ?
                MagicEvent.NONE :
                new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            player + " may return target creature card " +
                            "from his or her graveyard to the battlefield.",
                            MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD),
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{player},
                    this,
                    player + " may$ return target creature card$ from " +
                    "his or her graveyard to the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
                    }
                });
            }
        }
    };
}
