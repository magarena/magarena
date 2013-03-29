package magic.card;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;

public class Eaten_by_Spiders {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target creature$ with flying and " +
                    "all Equipment attached to that creature.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPermanentList perms = new MagicPermanentList();
                    perms.add(creature);
                    perms.addAll(creature.getEquipmentPermanents());
                    game.doAction(new MagicDestroyAction(perms));
                }
            });
        }
    };
}
