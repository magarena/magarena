package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPowerTargetPicker;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Wall_of_Reverence {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player=permanent.getController();
            return (player==data) ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                    MagicPowerTargetPicker.getInstance(),
                    new Object[]{player},
                    this,
                    player + " gains life equal to the power of target creature$ he or she controls."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],creature.getPower()));
                }
            });
        }
    };
}
