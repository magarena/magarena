package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilterFactory;

import java.util.Collection;

public class BattleCryTrigger extends ThisAttacksTrigger {

    private static final BattleCryTrigger INSTANCE = new BattleCryTrigger();

    private BattleCryTrigger() {
        super(8);
    }

    public static BattleCryTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
        return new MagicEvent(
                permanent,
                this,
                "Each other attacking creature gets +1/+0 until end of turn."
            );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent permanent = event.getPermanent();
        final Collection<MagicPermanent> targets = MagicTargetFilterFactory.ATTACKING_CREATURE.filter(event);
        for (final MagicPermanent creature : targets) {
            if (creature != permanent && creature.isAttacking()) {
                game.doAction(new ChangeTurnPTAction(creature,1,0));
            }
        }
    }
}
