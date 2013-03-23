package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Sleep {
    public static final MagicSpellCardEvent E =new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "Tap all creatures target player$ controls. Those creatures don't untap during their controller's next untap step.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final Collection<MagicPermanent> targets=
                        game.filterPermanents(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicPermanent creature : targets) {
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
