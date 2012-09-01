package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Demonic_Taskmaster {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data &&
                player.getNrOfPermanentsWithType(MagicType.Creature) > 1) ?    
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        "Sacrifice a creature other than " + permanent + ".") :
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
            if (player.controlsPermanentWithType(MagicType.Creature)) {
                final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                        permanent);
                final MagicTargetChoice targetChoice = new MagicTargetChoice(
                        targetFilter,
                        false,
                        MagicTargetHint.None,
                        "a creature other than " + permanent + " to sacrifice");
                game.addEvent(new MagicSacrificePermanentEvent(permanent,player,targetChoice));
            }        
        }
    };
}
