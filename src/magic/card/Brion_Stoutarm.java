package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class Brion_Stoutarm {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,MagicConditionFactory.ManaCost("{R}"),
                MagicCondition.TWO_CREATURES_CONDITION
            },
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicTargetFilter<MagicPermanent> targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,source);
            final MagicTargetChoice targetChoice=new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,"a creature other than " + source + " to sacrifice");
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(source,"{R}"),
                new MagicSacrificePermanentEvent(source,source.getController(),targetChoice)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicTarget sacrificed=payedCost.getTarget();
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                sacrificed,
                this,
                "SN deals damage equal to the power of "+sacrificed+" to target player$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicPermanent sacrificed=event.getRefPermanent();
                    final MagicDamage damage=new MagicDamage(event.getSource(),player,sacrificed.getPower());
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
