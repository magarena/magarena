package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicCleanupPlayerAction extends MagicAction {

    private final MagicPlayer player;
    private int oldPreventDamage;
    private int oldDrawnCards;
    private int oldLifeLost;
    private int oldLifeGained;

    public MagicCleanupPlayerAction(final MagicPlayer player) {
        this.player=player;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldPreventDamage=player.getPreventDamage();
        player.setPreventDamage(0);

        oldDrawnCards=player.getDrawnCards();
        player.setDrawnCards(0);
        
        oldLifeLost=player.getLifeLossThisTurn();
        player.setLifeLossThisTurn(0);
        
        oldLifeGained=player.getLifeGainThisTurn();
        player.setLifeGainThisTurn(0);
        

        for (final MagicPermanent permanent : player.getPermanents()) {
            if (permanent.isCreature()) {
                game.doAction(new MagicCleanupCreatureAction(permanent));
            } else {
                game.doAction(new MagicCleanupPermanentAction(permanent));
            }
        }

        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.setPreventDamage(oldPreventDamage);
        player.setDrawnCards(oldDrawnCards);
        player.setLifeGainThisTurn(oldLifeGained);
        player.setLifeLossThisTurn(oldLifeLost);
    }
}
