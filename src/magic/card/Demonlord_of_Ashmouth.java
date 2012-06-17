package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Demonlord_of_Ashmouth {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            final MagicTargetFilter targetFilter = 
                    new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
            final MagicTargetChoice targetChoice = 
                    new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,"another creature to sacrifice");
            final MagicChoice magicChoice = 
                    new MagicMayChoice(
                    "You may sacrifice another creature. If you don't," +
                    " exile " + permanent + ".",targetChoice);
            return new MagicEvent(
                    permanent,
                    player,
                    magicChoice,
                    MagicSacrificeTargetPicker.create(),
                    new Object[]{permanent},
                    this,
                    "You may$ sacrifice another creature$. If you don't," +
                    " exile " + permanent + ".");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicSacrificeAction(creature));
                    }
                });
            } else {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
            }
        }
    };
}
