package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public abstract class MagicLandfallTrigger extends MagicWhenOtherComesIntoPlayTrigger {
    public MagicLandfallTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicLandfallTrigger() {}
    
    protected abstract MagicEvent getEvent(final MagicPermanent permanent);
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
        return (played.isFriend(permanent) && played.isLand()) ?
            getEvent(permanent) :
            MagicEvent.NONE;
    }
}
