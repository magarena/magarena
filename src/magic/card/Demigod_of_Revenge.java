package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;

import java.util.List;

public class Demigod_of_Revenge {
    public static final MagicWhenSpellIsCastTrigger T = new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all cards named SN from your graveyard to the battlefield."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final List<MagicCard> cards = game.filterCards(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CARD_FROM_GRAVEYARD);
            for (final MagicCard card : cards) {
                if (card.getName().equals(event.getSource().getName())) {
                    game.doAction(new MagicReanimateAction(
                        event.getPlayer(),
                        card,
                        MagicPlayCardAction.NONE
                    ));
                }    
            }
        }
    };
}
