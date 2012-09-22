package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Sudden_Disappearance {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.TARGET_PLAYER,
                    this,
                    "Exile all nonland permanents target player$ controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final Collection<MagicTarget> targets =
                            game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT_YOU_CONTROL);
                    for (final MagicTarget target : targets) {
                        final MagicPermanent permanent = (MagicPermanent)target;
                        if (!permanent.isLand()) {
                            game.doAction(new MagicExileUntilEndOfTurnAction(permanent));    
                        }
                    }
                }
            });
        }
    };
}
