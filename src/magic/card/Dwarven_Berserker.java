package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Dwarven_Berserker {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (permanent == data ) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new Object[]{permanent},
                            this,
                            permanent + " gets +3/+0 and gains trample until end of turn."):
                    MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent creature = (MagicPermanent)data[0];
            game.doAction(new MagicChangeTurnPTAction(creature,3,0));
            game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
        }
    };
}
