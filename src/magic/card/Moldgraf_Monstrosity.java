package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

import java.util.List;

public class Moldgraf_Monstrosity {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Exile SN, then return two creature " +
                    "cards at random from your graveyard to the battlefield."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicRemoveCardAction(permanent.getCard(),MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(permanent.getCard(),MagicLocationType.Graveyard,MagicLocationType.Exile));
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> targets =
                    game.filterCards(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
            final magic.MersenneTwisterFast rng = 
                    new magic.MersenneTwisterFast(permanent.getId() + player.getId());
            int actualAmount = Math.min(targets.size(),2);
            for (;actualAmount>0;actualAmount--) {        
                final int index = rng.nextInt(targets.size());
                final MagicCard card = targets.get(index);
                game.doAction(new MagicReanimateAction(player,card,MagicPlayCardAction.NONE));
            }        
        }
    };
}
