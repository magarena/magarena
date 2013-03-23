package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Barter_in_Blood {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Each player sacrifices two creatures.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                for (int i=2;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        MagicTargetChoice.SACRIFICE_CREATURE));
                }
            }
        }
    };
}
