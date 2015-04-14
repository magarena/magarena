package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.choice.MagicDeclareAttackersResult;
import magic.model.trigger.MagicTriggerType;

public class DeclareAttackersAction extends MagicAction {

    private final MagicDeclareAttackersResult attackers;
    private final MagicPlayer active;

    public DeclareAttackersAction(final MagicPlayer player, final MagicDeclareAttackersResult result) {
        active = player;
        attackers = result;
    }

    @Override
    public void doAction(final MagicGame game) {
        // 508.1f The active player taps the chosen creatures.
        for (final MagicPermanent attacker : attackers) {
            if (!attacker.hasAbility(MagicAbility.Vigilance)) {
                game.doAction(new TapAction(attacker));
            }
        }
        // 508.1j Each chosen creature still controlled by the active player becomes an attacking creature.
        for (final MagicPermanent attacker : attackers) {
            if (attacker.getController() == active) {
                game.doAction(new DeclareAttackerAction(attacker));
            }
        }
        // 508.2. Any abilities that triggered on attackers being declared go on the stack.
        for (final MagicPermanent attacker : attackers) {
            if (attacker.hasState(MagicPermanentState.Attacking)) {
                game.executeTrigger(MagicTriggerType.WhenAttacks,attacker);
            }
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
