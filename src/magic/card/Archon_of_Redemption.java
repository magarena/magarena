package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Archon_of_Redemption {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,permanent},
                    this,
                    player + " gains life equal to " + permanent + "'s power.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent=(MagicPermanent)data[1];
            game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower()));
        }        
    };

    public static final MagicWhenOtherComesIntoPlayTrigger T2 = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player=permanent.getController();
            return (otherPermanent!=permanent && 
                    otherPermanent.getController()==player && 
                    otherPermanent.isCreature() &&
                    otherPermanent.hasAbility(MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,otherPermanent},
                        this,
                        player + " gains life equal to the power of "+otherPermanent+'.'):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent=(MagicPermanent)data[1];
            game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower()));
        }        
    };
}
