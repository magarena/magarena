package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;


public class Skirsdag_Cultist {
    public static final MagicPermanentActivation A =new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.ONE_CREATURE_CONDITION,
                MagicConditionFactory.ManaCost("{R}")
            },
            new MagicActivationHints(MagicTiming.Removal),
            "Exile") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPlayer player = source.getController();
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(source,player,MagicManaCost.create("{R}")),
                new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_CREATURE)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    this,
                    "SN deals 2 damage to target creature or player$.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),target,2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
