package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Essence_Harvest {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "Target player$ loses X life and you gain X life, where " +
                    "X is the greatest power among creatures PN controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    int power = 0;
                    for (final MagicPermanent creature : targets) {
                        power = Math.max(power,creature.getPower());
                    }
                    game.doAction(new MagicChangeLifeAction(player,-power));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),power));
                }
            });
        }
    };
}
