package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class MagicAnnihilatorTrigger extends MagicWhenAttacksTrigger {
    final private int amount;

    public MagicAnnihilatorTrigger(final int amount) {
        this.amount = amount;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPermanent creature) {
        final MagicPlayer opponent = permanent.getController().getOpponent();
        return (permanent == creature) ?
            new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent,opponent},
                    this,
                    opponent + " sacrifices " + amount +
                    (amount == 1 ? " permanent." : " permanents.")):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        final MagicPermanent permanent = (MagicPermanent)data[0];
        final MagicPlayer player = (MagicPlayer)data[1];
        int count = amount;
        while (count > 0 && player.getPermanents().size() > 0) {
            game.addEvent(new MagicSacrificePermanentEvent(
                    permanent,
                    player,
                    MagicTargetChoice.SACRIFICE_PERMANENT));
            count--;
        }
    }
}
