package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class ChangePoisonAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;
    private int oldPoison;

    public ChangePoisonAction(final MagicPlayer player,final int amount) {
        this.player=player;
        this.amount=amount;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldPoison=player.getPoison();
        final int newPoison=oldPoison+amount;
        player.setPoison(newPoison);
        setScore(player,ArtificialScoringSystem.getPoisonScore(newPoison)-ArtificialScoringSystem.getPoisonScore(oldPoison));
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.setPoison(oldPoison);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+player.getName()+','+amount+')';
    }
}
