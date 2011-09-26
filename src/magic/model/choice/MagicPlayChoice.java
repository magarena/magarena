package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;
import magic.model.event.MagicActivationMap;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicPhaseType;
import magic.ui.GameController;
import magic.ui.choice.PlayChoicePanel;

import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MagicPlayChoice extends MagicChoice {
	
	private static final MagicChoice INSTANCE=new MagicPlayChoice();

	private static final String MESSAGE="Play a card or ability.|Press {f} to pass priority.";
	private static final String CONTINUE_MESSAGE="Press {f} to pass priority.";
	
	private static final Collection<Object> PASS_OPTIONS=Collections.<Object>singleton(MagicPlayChoiceResult.SKIP);
	private static final Object PASS_CHOICE_RESULTS[]=new Object[]{MagicPlayChoiceResult.SKIP};
	
	private MagicPlayChoice() {
		super("Play a card or ability.");
	}
	
	@Override
	Collection<Object> getArtificialOptions(
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
		
		final ArrayList<Object> options=new ArrayList<Object>();

		// Pass is first choice when scores are equal.
		options.add(MagicPlayChoiceResult.PASS);

        // add rest of the options
        addValidChoices(game, player, true, options);

        // if single choice, skip instead of pass
        if (options.size() == 1) {
            options.set(0, MagicPlayChoiceResult.SKIP);
        }
		
		return options;
	}
	
	private static void addValidChoices(
            final MagicGame game,
            final MagicPlayer player,
            final boolean isAI,
            final Collection<Object> validChoices) {
		final MagicActivationMap activationMap=player.getActivationMap();
		for (final MagicActivation activation : activationMap.getActivations()) {
			final Set<MagicSource> sources=activationMap.get(activation);
			for (final MagicSource activationSource : sources) {
				if (activation.canPlay(game,player,activationSource,isAI)) {
                    if (isAI) { //only AI uses hints
                        validChoices.add(new MagicPlayChoiceResult(activationSource,activation));
                        if (activation.getActivationHints().isIndependent()) {
                            break;
                        }
                    } else {
    					validChoices.add(activationSource);
                    }
				}
			}
		}
	}
	
	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {

        //always pass draw and begin combat option
		if (game.canAlwaysPass()) {
			return PASS_CHOICE_RESULTS;
		} 
       
        //skip all combat phases if nobody is attacking
        if ((game.isPhase(MagicPhaseType.DeclareAttackers) ||
             game.isPhase(MagicPhaseType.DeclareBlockers) ||
             game.isPhase(MagicPhaseType.CombatDamage) ||
             game.isPhase(MagicPhaseType.EndOfCombat)) &&
            player.getNrOfAttackers() == 0 && 
            game.getOpponent(player).getNrOfAttackers() == 0) {
			return PASS_CHOICE_RESULTS;
        }

        //skip if phase is combat damage, not supposed to be able to do
        //anything but resolve triggers
        if (game.isPhase(MagicPhaseType.CombatDamage)) {
            if (!game.getStack().isEmpty()) {
                controller.pause(GeneralConfig.getInstance().getMessageDelay());
            }
			return PASS_CHOICE_RESULTS;
        }

		
		final Set<Object> validChoices = new HashSet<Object>();
        addValidChoices(game, player, false, validChoices);
	
        if (validChoices.isEmpty() && MagicGame.canSkipSingleChoice()) {
            boolean skip = true;

            //if AI blocks, don't skip priority so that user can observe how the AI is blocking
            if (game.isPhase(MagicPhaseType.DeclareBlockers) && 
                game.getOpponent(player).getNrOfBlockers() > 0 && 
                game.getStack().isEmpty()) {
                skip = false;
            }

            if (skip) {
                //pause if there is an item on the stack
                if (!game.getStack().isEmpty()) {
                    controller.pause(GeneralConfig.getInstance().getMessageDelay());
                }
    			return PASS_CHOICE_RESULTS;
            }
		}

        /*
        if ((game.getStack().isEmpty() || game.getStack().hasItemOnTopOfPlayer(player)) && 
             game.getTurnPlayer() == player &&                
             game.getPassPriority()) {
            if (!game.getStack().isEmpty()) {
                GameController.pause(1500);
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

        assert results.size() > 0 : "ERROR! There should be at least one activation possible.";
		
		if (results.size() == 1) {
            return new Object[]{results.get(0)};
        } else {
            controller.setSourceCardDefinition(activationSource);
            final PlayChoicePanel choicePanel = controller.showComponent(new Callable<PlayChoicePanel>() {
                public PlayChoicePanel call() {
                    return new PlayChoicePanel(controller,activationSource,results);
                }
            });
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
