package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Soul_Link {
    //deals damage
    public static final MagicWhenDamageIsDealtTrigger T1 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final int amount=damage.getDealtAmount();
            return (damage.getSource()==permanent.getEnchantedCreature()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{amount},
                        this,
                        player + " gains " + amount + " life.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),(Integer)data[0]));
        }
    };
    
    //dealt damage
    public static final MagicWhenDamageIsDealtTrigger T2 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final int amount=damage.getDealtAmount();
            return (damage.getTarget()==permanent.getEnchantedCreature()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{amount},
                    this,
                    player + " gains " + amount + " life.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),(Integer)data[0]));
        }
    };
}
