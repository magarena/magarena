package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public abstract class MagicLandfallTrigger extends MagicWhenOtherComesIntoPlayTrigger {
    public MagicLandfallTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicLandfallTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherComesIntoPlay;
    }

    protected abstract MagicEvent getEvent(final MagicPermanent permanent);
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
        final MagicPlayer player = permanent.getController();
        return (player == played.getController() && played.isLand()) ?
            getEvent(permanent) :
            MagicEvent.NONE;
    }
}
