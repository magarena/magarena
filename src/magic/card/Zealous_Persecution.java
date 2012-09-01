package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Zealous_Persecution {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack},
                    this,
                    "Until end of turn, creatures you control get +1/+1 and creatures your opponent controls get -1/-1.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            final MagicPlayer player=event.getPlayer();
            final Collection<MagicTarget> targets=
                game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature=(MagicPermanent)target;
                if (creature.getController()==player) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                } else {
                    game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
                }
            }
        }
    };
}
