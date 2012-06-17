package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Sleep {
    public static final MagicSpellCardEvent E =new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new Object[]{cardOnStack},
                    this,
                    "Tap all creatures target player$ controls. Those creatures don't untap during their controller's next untap step.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final Collection<MagicTarget> targets=
                        game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicTarget target : targets) {
                        final MagicPermanent creature=(MagicPermanent)target;
                        game.doAction(new MagicTapAction(creature,true));
                        game.doAction(new MagicChangeStateAction(
                                    creature,
                                    MagicPermanentState.DoesNotUntapDuringNext,
                                    true));
                    }
                }
            });
        }
    };
}
