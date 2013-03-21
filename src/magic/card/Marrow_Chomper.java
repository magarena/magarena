package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Marrow_Chomper {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final MagicTargetFilter<MagicPermanent> targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
            final MagicTargetChoice targetChoice=new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,"a creature other than "+permanent+" to sacrifice");
            return player.getNrOfPermanentsWithType(MagicType.Creature) > 1 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "PN may$ sacrifice a creature$ to SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public boolean usesStack() {
            return false;
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicPermanent permanent=event.getPermanent();
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicSacrificeAction(creature));
                        game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,2,true));
                        game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
                        final MagicEvent newEvent=executeTrigger(game,permanent,permanent.getController());
                        if (newEvent.isValid()) {
                            game.addEvent(newEvent);
                        }
                    }
                });
            } 
        }
    };
}
