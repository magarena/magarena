package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicDamage;
import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPayedCost;
import magic.model.action.MagicTargetAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.condition.MagicCondition;

public class MagicPingActivation extends MagicPermanentActivation {
    
    private final int n;
    
    public MagicPingActivation(final int n) {
         super(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage");
        this.n = n;
    }

    @Override
    public MagicEvent[] getCostEvent(final MagicSource source) {
        return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
                source,
                source.getController(),
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(n),
                new Object[]{source},
                this,
                source + " deals " + n + " damage to target creature or player$.");
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        event.processTarget(game,choiceResults,0,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,n,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        });
    }
}
