package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Psychic_Barrier {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE_SPELL,
                    this,
                    "Counter target creature spell$. Its controller loses 1 life.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                    game.doAction(new MagicChangeLifeAction(targetSpell.getController(),-1));
                }
            });
        }
    };
}
