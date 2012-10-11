package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.List;

public class Angel_of_Glory_s_Rise {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Exile all Zombies, then return all Human creature " +
                    "cards from your graveyard to the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicPermanent> zombies =
                    game.filterPermanents(player,MagicTargetFilter.TARGET_ZOMBIE);
            for (final MagicPermanent target : zombies) {
                game.doAction(new MagicRemoveFromPlayAction(
                        target,
                        MagicLocationType.Exile));
            }        
            final List<MagicCard> humans =
                    game.filterCards(player,MagicTargetFilter.TARGET_HUMAN_CARD_FROM_GRAVEYARD);
            for (final MagicCard target : humans) {
                game.doAction(new MagicReanimateAction(
                        player,
                        target,
                        MagicPlayCardAction.NONE));
            }
        }
    };
}
