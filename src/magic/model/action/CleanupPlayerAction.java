package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;

public class CleanupPlayerAction extends MagicAction {

    private final MagicPlayer player;
    private int oldPreventDamage;
    private int oldDrawnCards;
    private int oldLifeLost;
    private int oldLifeGained;
    private int oldCreaturesAttacked;
    private int oldStateFlags;

    public CleanupPlayerAction(final MagicPlayer player) {
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

        oldCreaturesAttacked=player.getCreaturesAttackedThisTurn();
        player.setCreaturesAttackedThisTurn(0);

        oldStateFlags=player.getStateFlags();
        player.setStateFlags(oldStateFlags & MagicPlayerState.CLEANUP_MASK);

        for (final MagicPermanent permanent : player.getPermanents()) {
            game.doAction(new CleanupPermanentAction(permanent));
        }

        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.setPreventDamage(oldPreventDamage);
        player.setDrawnCards(oldDrawnCards);
        player.setLifeGainThisTurn(oldLifeGained);
        player.setLifeLossThisTurn(oldLifeLost);
        player.setCreaturesAttackedThisTurn(oldCreaturesAttacked);
        player.setStateFlags(oldStateFlags);
    }
}
