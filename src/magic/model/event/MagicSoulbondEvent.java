package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSoulbondAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class MagicSoulbondEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicSoulbondAction(
                        (MagicPermanent)data[0],
                        (MagicPermanent)choiceResults[1],
                        true));
            }
        }
    };
    
    public MagicSoulbondEvent(final MagicPermanent permanent,final boolean hasSoulbond) {
        super(
            MagicEvent.NO_SOURCE,
            permanent.getController(),
            new MagicMayChoice(
                    permanent.getController() + " may pair two creatures (Soulbond).",
                    hasSoulbond ?
                        new MagicTargetChoice(
                            new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                                    MagicTargetFilter.TARGET_UNPAIRED_CREATURE_YOU_CONTROL,
                                    permanent),
                            false,
                            MagicTargetHint.None,
                            "a creature other than " + permanent) 
                        :
                        MagicTargetChoice.TARGET_UNPAIRED_SOULBOND_CREATURE),
            new Object[]{permanent},
            EVENT_ACTION,
            permanent.getController() + " may$ pair a creature$ with " + permanent + "."
        );
    }
}
