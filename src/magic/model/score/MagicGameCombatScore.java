package magic.model.score;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.CombatDamageAction;
import magic.model.action.DeclareBlockersAction;
import magic.model.action.MagicStackResolveAction;
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
        game.snapshot();
        game.doAction(new DeclareBlockersAction(defendingPlayer,result));
        game.doAction(new CombatDamageAction(attackingPlayer,defendingPlayer,true));
        game.doAction(new CombatDamageAction(attackingPlayer,defendingPlayer,false));
        // resolve triggers
        while (game.getStack().size() > 0 && !game.isFinished()) {
            game.doAction(new MagicStackResolveAction());
            game.checkState();
        }
        // Give extra points for extra blocked creatures.
        final int score=game.getScore()+result.size();
        game.restore();
        return score;
    }
}
