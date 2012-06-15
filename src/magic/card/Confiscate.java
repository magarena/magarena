package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveStaticAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicAddTriggerAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Confiscate {
    public static final Object S = new MagicStatic(MagicLayer.Control) {
        @Override
        public MagicPlayer getController(
                final MagicPermanent source,
                final MagicPermanent target,
                final MagicPlayer player) {
            return source.getController();
        }
        @Override
        public boolean accept(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return source.getEnchantedCreature() == target;
        }
    };
}
