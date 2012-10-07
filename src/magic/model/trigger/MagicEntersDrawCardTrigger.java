package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;

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
                this,
                "PN draws a card.");
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        game.doAction(new MagicDrawAction(event.getPlayer(),1));
    }
}

