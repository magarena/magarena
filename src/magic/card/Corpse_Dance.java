package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;

import java.util.List;

public class Corpse_Dance {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicBuybackChoice(MagicManaCost.create("{2}")),
                    new MagicGraveyardTargetPicker(true),
                    this,
                    "Return the top creature card$ of your graveyard to the battlefield. " +
                    "That creature gains haste until end of turn. " + 
                    "Exile it at the beginning of the next end step. " + 
                    "If the buyback cost was payed$, return SN to its owner's hand as it resolves.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> targets =
                    game.filterCards(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
            if (targets.size() > 0) {
                final MagicCard card = targets.get(targets.size()-1);
                game.doAction(new MagicReanimateAction(
                        player,
                        card,
                        MagicPlayCardAction.HASTE_UEOT_REMOVE_AT_END_OF_TURN));
            } 
            if (event.isBuyback()) {
                game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            } 
        }
    };
}
