package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Judge_of_Currents {
    public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            return (tapped.isFriend(permanent) &&
                    tapped.isCreature() &&
                    tapped.hasSubType(MagicSubType.Merfolk)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ gain 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
            }
        }
    };
}
