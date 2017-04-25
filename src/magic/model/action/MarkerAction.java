package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.event.MagicActivationPriority;
import magic.model.phase.MagicPhase;
import magic.model.phase.MagicStep;

public class MarkerAction extends MagicAction {

    private MagicPlayer oldTurnPlayer;
    private MagicPhase oldPhase;
    private MagicStep oldStep;
    private int oldTurn;
    private int oldMainPhaseCount;
    private int oldScore;
    private int oldLandsPlayed;
    private boolean oldCreatureDiedThisTurn;
    private boolean oldPriorityPassed;
    private int oldPriorityPassedCount;
    private boolean oldStateCheckRequired;

    private MagicPayedCost oldPayedCost;

    private int oldSpellsCast1;
    private int oldSpellsCast2;
    private int oldNonCreatureSpellsCast1;
    private int oldNonCreatureSpellsCast2;
    private int oldSpellsCastLastTurn1;
    private int oldSpellsCastLastTurn2;
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
        oldLandsPlayed=game.getLandsPlayed();
        oldCreatureDiedThisTurn = game.getCreatureDiedThisTurn();
        oldPriorityPassed=game.getPriorityPassed();
        oldPriorityPassedCount=game.getPriorityPassedCount();
        oldStateCheckRequired=game.getStateCheckRequired();
        oldPayedCost=game.getPayedCost();
        final MagicPlayer player1=game.getPlayer(0);
        oldActivationPriority1=player1.getActivationPriority();
        player1.setActivationPriority(new MagicActivationPriority(oldActivationPriority1));
        oldSpellsCast1=player1.getSpellsCast();
        oldNonCreatureSpellsCast1=player1.getNonCreatureSpellsCast();
        oldSpellsCastLastTurn1=player1.getSpellsCastLastTurn();
        final MagicPlayer player2=game.getPlayer(1);
        oldActivationPriority2=player2.getActivationPriority();
        player2.setActivationPriority(new MagicActivationPriority(oldActivationPriority2));
        oldSpellsCast2=player2.getSpellsCast();
        oldNonCreatureSpellsCast2=player2.getNonCreatureSpellsCast();
        oldSpellsCastLastTurn2=player2.getSpellsCastLastTurn();
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.setTurnPlayer(oldTurnPlayer);
        game.setPhase(oldPhase);
        game.setStep(oldStep);
        game.setTurn(oldTurn);
        game.setMainPhaseCount(oldMainPhaseCount);
        game.setScore(oldScore);
        game.setLandsPlayed(oldLandsPlayed);
        game.setCreatureDiedThisTurn(oldCreatureDiedThisTurn);
        game.setPriorityPassed(oldPriorityPassed);
        game.setPriorityPassedCount(oldPriorityPassedCount);
        game.setStateCheckRequired(oldStateCheckRequired);
        game.setPayedCost(oldPayedCost);
        final MagicPlayer player1=game.getPlayer(0);
        player1.setActivationPriority(oldActivationPriority1);
        player1.setSpellsCast(oldSpellsCast1);
        player1.setNonCreatureSpellsCast(oldNonCreatureSpellsCast1);
        player1.setSpellsCastLastTurn(oldSpellsCastLastTurn1);
        final MagicPlayer player2=game.getPlayer(1);
        player2.setActivationPriority(oldActivationPriority2);
        player2.setSpellsCast(oldSpellsCast2);
        player2.setNonCreatureSpellsCast(oldNonCreatureSpellsCast2);
        player2.setSpellsCastLastTurn(oldSpellsCastLastTurn2);

        game.update();
    }
}
