package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Mycoid_Shepherd {
    public static final MagicWhenPutIntoGraveyardTrigger T1 = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        5,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ gain 5 life.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),5));
            }
        }
    };

    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T2 = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent!=permanent &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature() && 
                    otherPermanent.getPower()>=5) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        5,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ gain 5 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),5));
            }
        }
    };
}
