package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;

public class MagicSingleActivationCondition implements MagicCondition {

    private MagicActivation act;

    /** This must be set by the activation. */
    public void setActivation(final MagicActivation aAct) {
        act = aAct;
    }

    @Override
    public boolean accept(final MagicSource source) {
        final MagicGame game = source.getGame();
        return !game.getStack().hasActivationOnTop(source,act);
    }
}
