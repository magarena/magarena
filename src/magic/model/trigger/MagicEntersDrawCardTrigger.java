package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class MagicEntersDrawCardTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicEntersDrawCardTrigger INSTANCE = new MagicEntersDrawCardTrigger();

    private MagicEntersDrawCardTrigger() {}

    public static MagicEntersDrawCardTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {
        return new MagicEvent(
                permanent,
                player,
                new Object[]{player},
                this,
                player + " draws a card.");
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
    }
}

