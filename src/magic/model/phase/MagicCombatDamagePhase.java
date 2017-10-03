package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.action.CombatDamageAction;
import magic.ui.MagicSound;

public class MagicCombatDamagePhase extends MagicPhase {

    public static final MagicPhase First = new MagicCombatDamagePhase(true);
    public static final MagicPhase Second = new MagicCombatDamagePhase(false);
    private final boolean isFirst;

    private MagicCombatDamagePhase(final boolean first) {
        super(MagicPhaseType.CombatDamage);
        isFirst = first;
    }

    @Override
    protected void executeBeginStep(final MagicGame game) {
        final MagicPlayer attackingPlayer = game.getTurnPlayer();
        final MagicPlayer defendingPlayer = attackingPlayer.getOpponent();
        final int defenderLifeBefore = defendingPlayer.getLife();
        final int defenderPoisonBefore = defendingPlayer.getPoison();
        final String defenderName = defendingPlayer.getName();
        final int attackerLifeBefore = attackingPlayer.getLife();
        final int attackerPoisonBefore = attackingPlayer.getPoison();
        final String attackerName = attackingPlayer.getName();

        final CombatDamageAction act = new CombatDamageAction(attackingPlayer, defendingPlayer, isFirst);
        game.doAction(act);

        //combat message for defender
        final int defenderLifeAfter = defendingPlayer.getLife();
        final int defenderPoisonAfter = defendingPlayer.getPoison();
        final StringBuilder defenderMessage = new StringBuilder();
        if (defenderLifeAfter > defenderLifeBefore) {
            defenderMessage.append(" gains ").append(defenderLifeAfter - defenderLifeBefore).append(" life.");
        } else if (defenderLifeAfter < defenderLifeBefore) {
            defenderMessage.append(" loses ").append(defenderLifeBefore - defenderLifeAfter).append(" life.");
        }
        if (defenderPoisonAfter > defenderPoisonBefore) {
            defenderMessage.append(" gets ").append(defenderPoisonAfter - defenderPoisonBefore).append(" poison counters.");
        }
        if (defenderMessage.length() > 0) {
            game.logMessage(defendingPlayer, "{c}" + defenderName + defenderMessage);
        }

        //combat message for attacker
        final int attackerLifeAfter = attackingPlayer.getLife();
        final int attackerPoisonAfter = attackingPlayer.getPoison();
        final StringBuilder attackerMessage = new StringBuilder();
        if (attackerLifeAfter > attackerLifeBefore) {
            attackerMessage.append(" gains ").append(attackerLifeAfter - attackerLifeBefore).append(" life.");
        } else if (attackerLifeAfter < attackerLifeBefore) {
            attackerMessage.append(" loses ").append(attackerLifeBefore - attackerLifeAfter).append(" life.");
        }
        if (attackerPoisonAfter > attackerPoisonBefore) {
            attackerMessage.append(" gets ").append(attackerPoisonAfter - attackerPoisonBefore).append(" poison counters.");
        }
        if (attackerMessage.length() > 0) {
            game.logMessage(attackingPlayer, "{c}" + attackerName + attackerMessage);
        }

        //End combat damage steps
        if (isFirst == false || act.dealtDamage()) {
            game.setStep(MagicStep.ActivePlayer);
        } else {
            game.setStep(MagicStep.NextPhase);
        }
        if (isFirst == false) {
            game.playSound(MagicSound.COMBAT);
        }
    }
}
