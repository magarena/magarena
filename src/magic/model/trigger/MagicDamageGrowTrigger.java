package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicDamageGrowTrigger extends MagicWhenDamageIsDealtTrigger {

    private final boolean combat;
    private final boolean player;

    public MagicDamageGrowTrigger(final boolean combat,final boolean player) {
        this.combat = combat;
        this.player = player;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicDamage damage) {
        return (damage.isSource(permanent) &&
                (!player || damage.getTarget().isPlayer()) &&
                (player || (damage.getTarget().isPermanent() &&
                damage.getTarget().isCreature())) &&
                (!combat || damage.isCombat())) ?
            new MagicEvent(
                permanent,
                this,
                "Put a +1/+1 counter on SN."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeCountersAction(
            event.getPermanent(),
            MagicCounterType.PlusOne,
            1
        ));
    }
}
