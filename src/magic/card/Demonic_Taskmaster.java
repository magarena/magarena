package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Demonic_Taskmaster {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a creature other than SN."
                ) :
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
            final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                permanent
            );
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter,
                false,
                MagicTargetHint.None,
                "a creature other than " + permanent + " to sacrifice"
            );
            game.addEvent(new MagicSacrificePermanentEvent(permanent,player,targetChoice));
        }
    };
}
