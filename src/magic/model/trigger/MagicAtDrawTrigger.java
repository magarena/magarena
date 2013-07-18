package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicCounterType;
import magic.model.event.MagicEvent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicSimpleMayChoice;

public abstract class MagicAtDrawTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtDrawTrigger(final int priority) {
        super(priority);
    }

    public MagicAtDrawTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtDraw;
    }

    public static final MagicAtDrawTrigger EachPlayerDraw = new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN draws a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player));
        }
    };
}
