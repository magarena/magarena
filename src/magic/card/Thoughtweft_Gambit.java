package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Thoughtweft_Gambit {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Tap all creatures your opponent controls and untap all creatures you control.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(player,MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent creature : targets) {
                if (creature.getController()==player) {
                    game.doAction(new MagicUntapAction(creature));
                } else {
                    game.doAction(new MagicTapAction(creature,true));
                }
            }
        }
    };
}
