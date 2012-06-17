package magic.card;

import java.util.List;

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
                    MagicEvent.NO_DATA,
                    this,
                    "Exile all Zombies, then return all Human creature " +
                    "cards from your graveyard to the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicTarget> zombies =
                    game.filterTargets(player,MagicTargetFilter.TARGET_ZOMBIE);
            for (final MagicTarget target : zombies) {
                game.doAction(new MagicRemoveFromPlayAction(
                        (MagicPermanent)target,
                        MagicLocationType.Exile));
            }        
            final List<MagicTarget> humans =
                    game.filterTargets(player,MagicTargetFilter.TARGET_HUMAN_CARD_FROM_GRAVEYARD);
            for (final MagicTarget target : humans) {
                game.doAction(new MagicReanimateAction(
                        player,
                        (MagicCard)target,
                        MagicPlayCardAction.NONE));
            }
        }
    };
}
