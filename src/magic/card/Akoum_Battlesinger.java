package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Akoum_Battlesinger {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may have Ally creatures he or " +
                                "she controls get +1/+0 until end of turn.",
                                MagicSimpleMayChoice.PUMP,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{player},
                        this,
                        player + " may$ have Ally creatures he or " +
                        "she controls get +1/+0 until end of turn.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final Collection<MagicTarget> targets =
                        game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_ALLY_YOU_CONTROL);
                    for (final MagicTarget target : targets) {
                        final MagicPermanent creature = (MagicPermanent)target;
                        game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                    }
            }            
        }        
    };
}
