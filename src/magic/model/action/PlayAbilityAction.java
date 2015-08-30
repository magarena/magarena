package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class PlayAbilityAction extends MagicAction {

    private final MagicPermanent permanent;

    public PlayAbilityAction(final MagicPermanent permanent) {
        this.permanent=permanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        permanent.incrementAbilityPlayedThisTurn();
    }

    @Override
    public void undoAction(final MagicGame game) {
        permanent.decrementAbilityPlayedThisTurn();
    }
}
