package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;

public class Demigod_of_Revenge {
    public static final MagicWhenSpellIsCastTrigger T = new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack data) {
            final MagicPlayer player = data.getController();
            return new MagicEvent(
                    data.getSource(),
                    player,
                    MagicEvent.NO_DATA,
                    this,
                    "Return all cards named " + data.getSource() +
                    " from your graveyard to the battlefield.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final List<MagicTarget> cards = game.filterTargets(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CARD_FROM_GRAVEYARD);
            for (final MagicTarget target : cards) {
                final MagicCard card = (MagicCard)target;
                if (card.getName().equals(event.getSource().getName())) {
                    game.doAction(new MagicReanimateAction(
                            event.getPlayer(),
                            card,
                            MagicPlayCardAction.NONE));
                }    
            }
        }
    };
}
