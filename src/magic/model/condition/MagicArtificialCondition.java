package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public class MagicArtificialCondition extends MagicCondition {

    private final MagicCondition normalCondition;
    private final MagicCondition artificialCondition;

    public MagicArtificialCondition(final MagicCondition normalCondition,final MagicCondition artificialCondition) {
        this.normalCondition=normalCondition;
        this.artificialCondition=artificialCondition;
    }

    public MagicArtificialCondition(final MagicCondition artificialCondition) {
        this(MagicCondition.NONE, artificialCondition);
    }

    @Override
    public boolean accept(final MagicSource source) {
        final MagicGame game = source.getGame();
        return game.isArtificial() ? 
            artificialCondition.accept(source) :
            normalCondition.accept(source);
    }
    
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        final MagicGame game = source.getGame();
        return game.isArtificial() ? 
            artificialCondition.getEvent(source) : 
            normalCondition.getEvent(source);
    }

    @Override
    public boolean isIndependent() {
        return artificialCondition.isIndependent() && normalCondition.isIndependent();
    }
}
