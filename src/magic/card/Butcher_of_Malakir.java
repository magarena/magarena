package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Butcher_of_Malakir {

    public static final MagicWhenPutIntoGraveyardTrigger T1 = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            final MagicPlayer controller = permanent.getController();
            return (MagicLocationType.Play==triggerData.fromLocation) ?
                new MagicEvent(
                    permanent,
                    controller,
                    new Object[]{permanent,game.getOpponent(controller)},
                    this,
                    "Your opponent sacrifices a creature."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer opponent=(MagicPlayer)data[1];
            if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                            (MagicPermanent)data[0],
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE));
            }
        }
    };
    
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T2 = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer controller = permanent.getController();
            return (otherPermanent != permanent && 
                    otherPermanent.getController() == controller && 
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    controller,
                    new Object[]{permanent,game.getOpponent(controller)},
                    this,
                    "Your opponent sacrifices a creature."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer opponent=(MagicPlayer)data[1];
            if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                            (MagicPermanent)data[0],
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE));
            }
        }
    };
}
