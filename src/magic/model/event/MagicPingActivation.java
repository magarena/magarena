package magic.model.event;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

import java.util.Arrays;

public class MagicPingActivation extends MagicPermanentActivation {

    private final int n;

    public MagicPingActivation(final int n) {
        super(
            new MagicActivationHints(MagicTiming.Removal),
            "Damage"
        );
        this.n = n;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(new MagicTapEvent(source));
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
            new MagicDamageTargetPicker(n),
            this,
            "SN deals " + n + " damage to target creature or player$."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTarget(game,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,n);
                game.doAction(new MagicDealDamageAction(damage));
            }
        });
    }
}
