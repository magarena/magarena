package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicRandom;
import magic.model.event.MagicEvent;
import magic.ui.GameController;

public class MagicDeclareBlockersChoice extends MagicChoice {

	private static final MagicDeclareBlockersChoice INSTANCE=new MagicDeclareBlockersChoice();
	
	private static final String BLOCKER_MESSAGE="Click on a creature to declare as blocker or remove from combat.|Press {f} to continue.";
	private static final String ATTACKER_MESSAGE="Click on an attacking creature to declare as blocker.";
	private static final String CONTINUE_MESSAGE="Press {f} to continue.";
	
	private MagicDeclareBlockersChoice() {
		
		super("Declare blockers.");
	}
	
	@Override
	public Collection<Object> getArtificialOptions(final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source) {
		
		final MagicDeclareBlockersResultBuilder builder=new MagicDeclareBlockersResultBuilder(
                game,player,game.getFastChoices());
		return builder.buildResults();
	}
	
	/** Builds result and does cleanup for blockers. */
	private void buildResult(
            final MagicCombatCreatureBuilder builder,
            final MagicDeclareBlockersResult result) {

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
					blocker.setBlockedCreature(null);
				}
				attacker.permanent.removeBlockingCreatures();
				result.add(creatures.toArray(new MagicCombatCreature[creatures.size()]));
			}
		}
	}

	@Override
	public Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,final MagicPlayer player,final MagicSource source) {

		final MagicDeclareBlockersResult result=new MagicDeclareBlockersResult(0,0);
		final MagicCombatCreatureBuilder builder=new MagicCombatCreatureBuilder(game,game.getOpponent(player),player);
		builder.buildBlockers();

		if (!builder.buildBlockableAttackers()&&game.canSkipDeclareBlockersSingleChoice()) {
			return new Object[]{result};
		}
		
		while (true) {

			// Choose blocker.
			final Set<MagicPermanent> candidateBlockers=builder.getCandidateBlockers();
			controller.focusViewers(-1,1);
			if (candidateBlockers.isEmpty()) {
				controller.showMessage(source,CONTINUE_MESSAGE);
			} else {
				controller.setValidChoices(new HashSet<Object>(candidateBlockers),true);
				controller.showMessage(source,BLOCKER_MESSAGE);
			}
			controller.enableForwardButton();
			if (controller.waitForInputOrUndo()) {
				buildResult(builder,result); // For cleanup.
				return UNDO_CHOICE_RESULTS;
			}
			controller.clearValidChoices();			
			if (controller.isActionClicked()) {
				buildResult(builder,result);
				break;
			}
			
			final MagicPermanent blocker=(MagicPermanent)controller.getChoiceClicked();
			// Remove blocker from combat.
			if (blocker.isBlocking()) {		
				final MagicPermanent attacker=blocker.getBlockedCreature();
				attacker.removeBlockingCreature(blocker);
				if (attacker.getBlockingCreatures().isEmpty()) {
					attacker.clearState(MagicPermanentState.Blocked);
				}
				blocker.setBlockedCreature(null);
				blocker.clearState(MagicPermanentState.Blocking);
			// Block an attacker.
			} else {
				controller.setSourceCardDefinition(blocker);
				controller.setValidChoices(new HashSet<Object>(builder.getBlockableAttackers(blocker)),true);
				controller.showMessage(blocker,ATTACKER_MESSAGE);
				controller.disableActionButton(false);
				if (controller.waitForInputOrUndo()) {
					return UNDO_CHOICE_RESULTS;
				}
				controller.setSourceCardDefinition(null);
				controller.clearValidChoices();					
				final MagicPermanent attacker=(MagicPermanent)controller.getChoiceClicked();
				attacker.addBlockingCreature(blocker);
				blocker.setState(MagicPermanentState.Blocking);
				blocker.setBlockedCreature(attacker);
			}
			controller.update();
		}

		game.createUndoPoint();
		return new Object[]{result};
	}
	
	@Override
    public Object[] getSimulationChoiceResult(
			final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		
        final MagicDeclareBlockersResult result=new MagicDeclareBlockersResult(0,0);
		final MagicCombatCreatureBuilder builder=new MagicCombatCreatureBuilder(game,game.getOpponent(player),player);
		builder.buildBlockers();

		if (!builder.buildBlockableAttackers()) {
			return new Object[]{result};
		}
			
        final Set<MagicPermanent> blockers = builder.getCandidateBlockers();
        for (final MagicPermanent blocker : blockers) {
	        MagicPermanent[] attackers = builder.getBlockableAttackers(blocker).toArray(new MagicPermanent[]{});
            final int idx = MagicRandom.nextInt(attackers.length + 1);
            if (idx < attackers.length) {
                MagicPermanent attacker = attackers[idx];
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
