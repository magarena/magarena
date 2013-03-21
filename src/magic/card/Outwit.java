package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Outwit {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_SPELL,
                    this,
                    "Counter target spell$ that targets a player.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    if (targetSpell.getChoiceResults() != null) {
                        for (final Object choiceResult : targetSpell.getChoiceResults()) {
                            for (final MagicPlayer player : game.getPlayers()) {
                                if (choiceResult == player) {
                                    game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    };
}
