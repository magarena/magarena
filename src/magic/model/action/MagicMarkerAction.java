package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.event.MagicActivationPriority;
import magic.model.phase.MagicPhase;
import magic.model.phase.MagicStep;

public class MagicMarkerAction extends MagicAction {

    private MagicPlayer oldTurnPlayer;
    private MagicPhase oldPhase;
    private MagicStep oldStep;
    private int oldTurn;
    private int oldMainPhaseCount;
    private int oldScore;
    private int oldLandPlayed;
    private int oldSpellsPlayed;
    private int oldSpellsPlayedLastTurn;
    private boolean oldCreatureDiedThisTurn;
    private boolean oldPriorityPassed;
    private int oldPriorityPassedCount;
    private boolean oldStateCheckRequired;

    private MagicPayedCost oldPayedCost;

    private int oldLife1;
    private int oldLife2;
    private int oldPoison1;
    private int oldPoison2;
    private MagicActivationPriority oldActivationPriority1;
    private MagicActivationPriority oldActivationPriority2;

    @Override
    public void doAction(final MagicGame game) {
        oldTurnPlayer=game.getTurnPlayer();
        oldPhase=game.getPhase();
        oldStep=game.getStep();
        oldTurn=game.getTurn();
        oldMainPhaseCount=game.getMainPhaseCount();
        oldScore=game.getScore();
        oldLandPlayed=game.getLandPlayed();
        oldSpellsPlayed = game.getSpellsPlayed();
        oldSpellsPlayedLastTurn = game.getSpellsPlayedLastTurn();
        oldCreatureDiedThisTurn = game.getCreatureDiedThisTurn();
        oldPriorityPassed=game.getPriorityPassed();
        oldPriorityPassedCount=game.getPriorityPassedCount();
        oldStateCheckRequired=game.getStateCheckRequired();
        oldPayedCost=game.getPayedCost();
        final MagicPlayer player1=game.getPlayer(0);
        oldLife1=player1.getLife();
        oldPoison1=player1.getPoison();
        oldActivationPriority1=player1.getActivationPriority();
        player1.setActivationPriority(new MagicActivationPriority(oldActivationPriority1));
        final MagicPlayer player2=game.getPlayer(1);
        oldLife2=player2.getLife();
        oldPoison2=player2.getPoison();
        oldActivationPriority2=player2.getActivationPriority();
        player2.setActivationPriority(new MagicActivationPriority(oldActivationPriority2));
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.setTurnPlayer(oldTurnPlayer);
        game.setPhase(oldPhase);
        game.setStep(oldStep);
        game.setTurn(oldTurn);
        game.setMainPhaseCount(oldMainPhaseCount);
        game.setScore(oldScore);
        game.setLandPlayed(oldLandPlayed);
        game.setSpellsPlayed(oldSpellsPlayed);
        game.setSpellsPlayedLastTurn(oldSpellsPlayedLastTurn);
        game.setCreatureDiedThisTurn(oldCreatureDiedThisTurn);
        game.setPriorityPassed(oldPriorityPassed);
        game.setPriorityPassedCount(oldPriorityPassedCount);
        game.setStateCheckRequired(oldStateCheckRequired);
        game.setPayedCost(oldPayedCost);
        final MagicPlayer player1=game.getPlayer(0);
        player1.setLife(oldLife1);
        player1.setPoison(oldPoison1);
        player1.setActivationPriority(oldActivationPriority1);
        final MagicPlayer player2=game.getPlayer(1);
        player2.setLife(oldLife2);
        player2.setPoison(oldPoison2);
        player2.setActivationPriority(oldActivationPriority2);

        game.update();
    }
}
