package magic.model.score;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.CombatDamageAction;
import magic.model.action.DeclareBlockersAction;
import magic.model.action.StackResolveAction;
import magic.model.choice.MagicDeclareBlockersResult;

public class MagicGameCombatScore implements MagicCombatScore {

    private final MagicGame game;
    private final MagicPlayer attackingPlayer;
    private final MagicPlayer defendingPlayer;

    public MagicGameCombatScore(final MagicGame game,final MagicPlayer attackingPlayer,final MagicPlayer defendingPlayer) {
        this.game=game;
        this.attackingPlayer=attackingPlayer;
        this.defendingPlayer=defendingPlayer;
    }

    @Override
    public int getScore(final MagicDeclareBlockersResult result) {
        // immediate mode for triggers
        game.setImmediate(true);
        game.snapshot();
        game.setScore(0);
        game.doAction(new DeclareBlockersAction(defendingPlayer,result));
        game.doAction(new CombatDamageAction(attackingPlayer,defendingPlayer,true));
        game.doAction(new CombatDamageAction(attackingPlayer,defendingPlayer,false));
        // resolve triggers
        game.checkStatePutTriggers();
        int resolved = 0;
        while (game.getStack().size() > 0 && resolved < 100 && !game.isFinished()) {
            resolved++;
            game.doAction(new StackResolveAction());
            game.checkStatePutTriggers();
        }
        // Give extra points for extra blocked creatures.
        final int score=game.getScore()+result.size();
        game.restore();
        game.setImmediate(false);
        return score;
    }
}
