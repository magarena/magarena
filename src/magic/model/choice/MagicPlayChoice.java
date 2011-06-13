package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;
import magic.model.event.MagicActivationMap;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicPhaseType;
import magic.ui.GameController;
import magic.ui.choice.PlayChoicePanel;

public class MagicPlayChoice extends MagicChoice {
	
	private static final MagicChoice INSTANCE=new MagicPlayChoice();

	private static final String MESSAGE="Choose a card or ability to play.|Press {f} to pass priority.";
	private static final String CONTINUE_MESSAGE="Press {f} to pass priority.";
	
	private static final Collection<Object> PASS_OPTIONS=Collections.<Object>singleton(MagicPlayChoiceResult.PASS);
	private static final Object PASS_CHOICE_RESULTS[]=new Object[]{MagicPlayChoiceResult.PASS};
	
	private MagicPlayChoice() {
		super("Choose a card or ability to play.");
	}
	
	@Override
	public Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

		// When something is already on top of stack for the player, always pass.
		if (game.getStack().hasItemOnTopOfPlayer(player)) {
			return PASS_OPTIONS;
		}

        //can't play spells or abilities during combat damage phase
        if (game.isPhase(MagicPhaseType.CombatDamage)) {
            return PASS_OPTIONS;
        }
		
		final List<Object> options=new ArrayList<Object>();
		final MagicActivationMap activationMap=player.getActivationMap();

		// Pass is first choice when scores are equal.
		options.add(MagicPlayChoiceResult.PASS);
		
		for (final MagicActivation activation : activationMap.getActivations()) {
			final Set<MagicSource> sources=activationMap.get(activation);
			for (final MagicSource activationSource : sources) {
				if (activation.canPlay(game,player,activationSource,true)) {
					options.add(new MagicPlayChoiceResult(activationSource,activation));
					if (activation.getActivationHints().isIndependent()) {
						break;
					}
				}
			}
		}
		
		return options;
	}
	
	private Set<Object> getValidChoices(final MagicGame game,final MagicPlayer player) {
		final Set<Object> validChoices=new HashSet<Object>();

		final MagicActivationMap activationMap=player.getActivationMap();
		for (final MagicActivation activation : activationMap.getActivations()) {
			final Set<MagicSource> sources=activationMap.get(activation);
			for (final MagicSource activationSource : sources) {
				if (activation.canPlay(game,player,activationSource,false)) {
					validChoices.add(activationSource);
				}
			}
		}
		return validChoices;
	}
	
	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {

		if (game.canAlwaysPass()) {
			return PASS_CHOICE_RESULTS;
		} 
        
        if ((game.isPhase(MagicPhaseType.DeclareAttackers) ||
             game.isPhase(MagicPhaseType.DeclareBlockers) ||
             game.isPhase(MagicPhaseType.CombatDamage) ||
             game.isPhase(MagicPhaseType.EndOfCombat)) &&
            player.getNrOfAttackers() == 0 && 
            game.getOpponent(player).getNrOfAttackers() == 0) {
			return PASS_CHOICE_RESULTS;
        }
		
		final Set<Object> validChoices;
        validChoices=getValidChoices(game,player);
		
        if ((game.isPhase(MagicPhaseType.CombatDamage)) ||
            (validChoices.isEmpty() && game.canSkipSingleChoice())) {
            if (!game.getStack().isEmpty()) {
                try {
                     Thread.sleep(1000);
                } catch (final Exception err) {

                }
            }
			return PASS_CHOICE_RESULTS;
		}

        /*
        if ((game.getStack().isEmpty() || game.getStack().hasItemOnTopOfPlayer(player)) && 
             game.getTurnPlayer() == player &&                
             game.getPassPriority()) {
            if (!game.getStack().isEmpty()) {
                try {
                     Thread.sleep(1000);
                } catch (final Exception err) {

                }
            }
            return PASS_CHOICE_RESULTS;
        }
        */

		controller.focusViewers(0,0);
		if (validChoices.isEmpty()) {
			controller.showMessage(source,CONTINUE_MESSAGE);
		} else {
			controller.showMessage(source,MESSAGE);
			controller.setValidChoices(validChoices,false);
		}
		controller.enableForwardButton();
		if (controller.waitForInputOrUndo()) {
			return UNDO_CHOICE_RESULTS;
		}
		controller.clearValidChoices();
		controller.disableActionButton(false);
		game.createUndoPoint();

		if (controller.isActionClicked()) {
			return PASS_CHOICE_RESULTS;
		}
		
		final MagicSource activationSource=(MagicSource)controller.getChoiceClicked();
		final List<MagicPlayChoiceResult> results=new ArrayList<MagicPlayChoiceResult>();
		for (final MagicActivation activation : activationSource.getActivations()) {
			
			if (activation.canPlay(game,player,activationSource,false)) {
				results.add(new MagicPlayChoiceResult(activationSource,activation));
			}
		}
		
		switch (results.size()) {
			case 0: 
				throw new IllegalStateException("There should be at least one activation possible.");
			case 1:
				return new Object[]{results.get(0)};
			default: 
				final PlayChoicePanel choicePanel=new PlayChoicePanel(controller,activationSource,results);
				controller.setSourceCardDefinition(activationSource);
				controller.showComponent(choicePanel);
				if (controller.waitForInputOrUndo()) {
					return UNDO_CHOICE_RESULTS;
				}
				return new Object[]{choicePanel.getResult()};
		}
	}

	public static MagicChoice getInstance() {
		return INSTANCE;
	}
}
