package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.List;

public class Zombie_Apocalypse {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Return all Zombie creature cards from your graveyard " +
                    "to the battlefield tapped, then destroy all Humans.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicTarget> zombies =
                    game.filterTargets(player,MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD);
            for (final MagicTarget target : zombies) {
                game.doAction(new MagicReanimateAction(
                        player,
                        (MagicCard)target,
                        MagicPlayCardAction.TAPPED));
            }
            final List<MagicTarget> humans =
                    game.filterTargets(player,MagicTargetFilter.TARGET_HUMAN);
            game.doAction(new MagicDestroyAction(humans));
        }
    };
}
