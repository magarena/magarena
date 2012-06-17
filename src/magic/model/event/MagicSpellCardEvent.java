package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicChangeCardDefinition;

public abstract class MagicSpellCardEvent implements MagicCardEvent,MagicEventAction,MagicChangeCardDefinition {

    private MagicCardDefinition cdef;
    
    public MagicSpellCardEvent() {}

    public void setCardDefinition(final MagicCardDefinition cdef) {
        this.cdef = cdef;
    }
    
    public final MagicCardDefinition getCardDefinition() {
        return cdef;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game, 
            final MagicEvent event, 
            final Object data[], 
            final Object[] choiceResults) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }
   
    @Override
    public void change(MagicCardDefinition cdef) {
        cdef.addEvent(this);
        setCardDefinition(cdef);
    }
}
