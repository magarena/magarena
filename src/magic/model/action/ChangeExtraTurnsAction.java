package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class ChangeExtraTurnsAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;

    public ChangeExtraTurnsAction(final MagicPlayer player,final int amount) {

        this.player=player;
        this.amount=amount;
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
