package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public class MagicArtificialCondition extends MagicCondition {

    private final MagicCondition cond;

    public MagicArtificialCondition(final MagicCondition artificialCondition) {
        cond = artificialCondition;
    }

    @Override
    public boolean accept(final MagicSource source) {
        return source.getGame().isArtificial() ? cond.accept(source) : true;
    }
    
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return cond.getEvent(source);
    }

    @Override
    public boolean isIndependent() {
        return cond.isIndependent();
    }
}
