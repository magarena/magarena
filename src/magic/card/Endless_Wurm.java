package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Endless_Wurm {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        "Sacrifice " + permanent + " unless you sacrifice an enchantment."):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            if (player.controlsPermanentWithType(MagicType.Enchantment)) {
                game.addEvent(new MagicSacrificePermanentEvent(permanent,player,MagicTargetChoice.SACRIFICE_ENCHANTMENT));
            } else {
                game.doAction(new MagicSacrificeAction(permanent));                
            }            
        }
    };
}
