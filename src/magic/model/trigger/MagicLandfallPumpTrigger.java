package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicLandfallTrigger;

public class MagicLandfallPumpTrigger extends MagicLandfallTrigger {
    private final int power;
    private final int toughness;
    
    public MagicLandfallPumpTrigger(final int power, final int toughness) {
        this.power = power;
        this.toughness = toughness;
    }

    @Override
    protected MagicEvent getEvent(final MagicPermanent permanent) {
        return new MagicEvent(
                permanent,
                permanent.getController(),
                MagicEvent.NO_DATA,
                this,
                permanent + " gets " + getString(power) + 
                "/" + getString(toughness) + " until end of turn.");
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        game.doAction(new MagicChangeTurnPTAction(
                (MagicPermanent)event.getSource(),
                power,
                toughness));
    }
    
    private String getString(final int pt) {
        return pt >= 0 ?
                "+" + pt :
                Integer.toString(pt);
    }
}
