package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public class MagicDeclareBlockersChoice extends MagicChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Declare blockers.";
    private static final String _S_BLOCKER_MESSAGE = "Click on a creature to declare as blocker or remove from combat.|Press {f} to continue.";
    private static final String _S_ATTACKER_MESSAGE = "Click on an attacking creature to declare as blocker.";
    private static final String _S_CONTINUE_MESSAGE = "Press {f} to continue.";

    private static final MagicDeclareBlockersChoice INSTANCE=new MagicDeclareBlockersChoice();

    private MagicDeclareBlockersChoice() {
        super(_S1);
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game,final MagicEvent event) {
        final MagicDeclareBlockersResultBuilder builder = new MagicDeclareBlockersResultBuilder(game, event.getPlayer(), game.getFastBlocker());
        return builder.getResults();
    }

    /** Builds result and does cleanup for blockers. */
    private static void buildResult(final MagicCombatCreatureBuilder builder, final MagicDeclareBlockersResult result) {
        for (final MagicCombatCreature attacker : builder.getAttackers()) {
            final MagicPermanentList blockers=attacker.permanent.getBlockingCreatures();
            if (!blockers.isEmpty()) {
                final List<MagicCombatCreature> creatures=new ArrayList<MagicCombatCreature>();
                creatures.add(attacker);
                for (final MagicPermanent blocker : blockers) {
                    for (final MagicCombatCreature candidateBlocker : attacker.candidateBlockers) {
                        if (candidateBlocker.permanent==blocker) {
                            creatures.add(candidateBlocker);
                            break;
                        }
                    }
                    blocker.clearState(MagicPermanentState.Blocking);
                    blocker.setBlockedCreature(MagicPermanent.NONE);
                }
                attacker.permanent.removeBlockingCreatures();
                result.add(creatures.toArray(new MagicCombatCreature[0]));
            }
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final MagicDeclareBlockersResult result=new MagicDeclareBlockersResult(0,0);
        final MagicCombatCreatureBuilder builder=new MagicCombatCreatureBuilder(game,player.getOpponent(),player);
        builder.buildBlockers();

        if (!builder.buildBlockableAttackers()&&game.canSkipDeclareBlockersSingleChoice()) {
            return new Object[]{result};
        }

        try {
            while (true) {
                // Choose blocker.
                final Set<MagicPermanent> candidateBlockers=builder.getCandidateBlockers();
                controller.focusViewers(-1);
                if (candidateBlockers.isEmpty()) {
                    controller.showMessage(source,_S_CONTINUE_MESSAGE);
                } else {
                    controller.setValidChoices(new HashSet<Object>(candidateBlockers),true);
                    controller.showMessage(source,_S_BLOCKER_MESSAGE);
                }
                controller.enableForwardButton();
                controller.waitForInput();
                controller.clearValidChoices();
                if (controller.isActionClicked()) {
                    break;
                }

                final MagicPermanent blocker = controller.getChoiceClicked();
                // Remove blocker from combat.
                if (blocker.isBlocking()) {
                    final MagicPermanent attacker=blocker.getBlockedCreature();
                    attacker.removeBlockingCreature(blocker);
                    if (attacker.getBlockingCreatures().isEmpty()) {
                        attacker.clearState(MagicPermanentState.Blocked);
                    }
                    blocker.setBlockedCreature(MagicPermanent.NONE);
                    blocker.clearState(MagicPermanentState.Blocking);
                // Block an attacker.
                } else {
                    controller.setSourceCardDefinition(blocker);
                    controller.setValidChoices(new HashSet<Object>(builder.getBlockableAttackers(blocker)),true);
                    controller.showMessage(blocker,_S_ATTACKER_MESSAGE);
                    controller.disableActionButton(false);
                    controller.waitForInput();
                    controller.setSourceCardDefinition(MagicSource.NONE);
                    controller.clearValidChoices();
                    final MagicPermanent attacker = controller.getChoiceClicked();
                    attacker.addBlockingCreature(blocker);
                    blocker.setState(MagicPermanentState.Blocking);
                    blocker.setBlockedCreature(attacker);
                }
                controller.updateGameView();
            }
        } finally {
            // Cleanup
            buildResult(builder,result);
        }

        game.snapshot();
        return new Object[]{result};
    }

    @Override
    public Object[] getSimulationChoiceResult(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();

        final MagicDeclareBlockersResult result=new MagicDeclareBlockersResult(0,0);
        final MagicCombatCreatureBuilder builder=new MagicCombatCreatureBuilder(game,player.getOpponent(),player);

        //check if any of the defending creatures can block
        if (!builder.buildBlockers()) {
            return new Object[]{result};
        }

        //check if non of the attackers can be blocked
        if (!builder.buildBlockableAttackers()) {
            return new Object[]{result};
        }

        final Set<MagicPermanent> blockers = builder.getCandidateBlockers();
        for (final MagicPermanent blocker : blockers) {
            final MagicPermanent[] attackers = builder.getBlockableAttackers(blocker).toArray(new MagicPermanent[0]);
            //choose one of the attackers or don't block
            final int idx = MagicRandom.nextRNGInt(attackers.length + 1);
            if (idx < attackers.length) {
                final MagicPermanent attacker = attackers[idx];
                attacker.addBlockingCreature(blocker);
                blocker.setState(MagicPermanentState.Blocking);
                blocker.setBlockedCreature(attacker);
            }
        }

        buildResult(builder,result);
        return new Object[]{result};
    }

    public static MagicDeclareBlockersChoice getInstance() {
        return INSTANCE;
    }

}
