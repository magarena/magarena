package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Soltari_Visionary {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource() == permanent &&
                damage.getTarget().isPlayer()) {
                final MagicPlayer target = (MagicPlayer)damage.getTarget();
                if (target.controlsPermanentWithType(MagicType.Enchantment)) {
                    final MagicPlayer player = permanent.getController();
                    return new MagicEvent(
                        permanent,
                        player,
                        damage.getTarget() == player ?
                            MagicTargetChoice.TARGET_ENCHANTMENT_YOU_CONTROL :
                            MagicTargetChoice.TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS,
                        new MagicDestroyTargetPicker(false),
                        MagicEvent.NO_DATA,
                        this,
                        "Destroy target enchantment$.");
                }
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicDestroyAction(perm));
                }
            });
        }
    };
}
