package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicLandfallTrigger;

public class Admonition_Angel {
    public static final MagicLandfallTrigger T1 = new MagicLandfallTrigger() {
        @Override
        protected MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicTargetFilter<MagicPermanent> targetFilter = 
                    new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                        MagicTargetFilter.TARGET_NONLAND_PERMANENT,
                        permanent);
            final MagicTargetChoice targetChoice = 
                    new MagicTargetChoice(
                        targetFilter,
                        true,
                        MagicTargetHint.None,
                        "another target nonland permanent to exile");
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicExileTargetPicker.create(),
                this,
                "PN may$ exile another target nonland permanent$.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicExileUntilThisLeavesPlayAction(event.getPermanent(),target));
                    }
                });
            }
        }        
    };
}
