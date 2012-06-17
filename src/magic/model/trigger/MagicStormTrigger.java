package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicStormTrigger extends MagicWhenSpellIsCastTrigger {

    private static final MagicStormTrigger INSTANCE = new MagicStormTrigger();

    private MagicStormTrigger() {}

    public static final MagicStormTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicCardOnStack data) {
        final MagicPlayer player = data.getController();
        final int count = game.getSpellsPlayed();
        return (count > 0) ?
                new MagicEvent(
                        data.getSource(),
                        player,
                        new Object[]{player,data,count},
                        this,
                        "Copy " + data.getSource() + " " +
                        count + (count == 1 ? " time." : " times.")) :
                MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        int count = (Integer)data[2];
        for (;count>0;count--) {
            game.doAction(new MagicCopyCardOnStackAction(
                    (MagicPlayer)data[0],
                    (MagicCardOnStack)data[1]));
        }
    }
    
    @Override
    public boolean usesStack() {
        return false;
    }
}
