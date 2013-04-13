package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Rakish_Heir {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicSource dmgSource = damage.getSource();
            return (damage.isCombat() && 
                    damage.getTarget().isPlayer() &&
                    dmgSource.getController() == player &&
                    dmgSource.isPermanent() &&
                    dmgSource.hasSubType(MagicSubType.Vampire)) ?
                new MagicEvent(
                        permanent,
                        player,
                        dmgSource,
                        this,
                        "Put a +1/+1 counter on " + dmgSource + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1,true));
        }
    };
}
