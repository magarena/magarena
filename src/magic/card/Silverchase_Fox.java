package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostSacrificeEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDestroyTargetPicker;

public class Silverchase_Fox {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ManaCost("{1}{W}")},
            new MagicActivationHints(MagicTiming.Removal),
            "Exile") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostSacrificeEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{1}{W}")
                )
            };
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                new MagicDestroyTargetPicker(false),
                this,
                "Exile target enchantment$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
                }
            });
        }
    };
}
