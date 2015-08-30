package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class ChangeExtraTurnsAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;

    public ChangeExtraTurnsAction(final MagicPlayer aPlayer,final int aAmount) {
        player = aPlayer;
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
        player.changeExtraTurns(amount);
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.changeExtraTurns(-amount);
    }
}
