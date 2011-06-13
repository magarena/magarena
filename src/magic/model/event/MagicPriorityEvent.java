package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicPlayChoice;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.phase.MagicStep;
import magic.model.phase.MagicPhaseType;

public class MagicPriorityEvent extends MagicEvent {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			
			final MagicPlayChoiceResult playChoiceResult=(MagicPlayChoiceResult)choiceResults[0];
            if (game.isPhase(MagicPhaseType.CombatDamage)) {
		        if (game.getStack().isEmpty()) {
                    //go back to begin and deal regular damage
                    game.setStep(MagicStep.Begin);
                } else {
                    //resolve triggers due to first strike
                    game.setStep(MagicStep.Resolve);
                }
            } else if (playChoiceResult==MagicPlayChoiceResult.PASS) {
				game.incrementPriorityPassedCount();
				// When passing, the last played activation can no longer be played.
				game.getPriorityPlayer().getActivationPriority().activationId++;
				if (game.getPriorityPassed()) {
					game.setPriorityPassed(false);
					game.resolve();
				} else {
					game.setPriorityPassed(true);
					switch (game.getStep()) {
						case ActivePlayer:
							game.setStep(MagicStep.OtherPlayer);
							break;
						case OtherPlayer:
							game.setStep(MagicStep.ActivePlayer);
							break;
						default:
							throw new IllegalStateException("Can not pass in "+game.getStep());
					}
				}
			} else {
				// Clear priority passed only when stack is used.
				final MagicActivation activation=playChoiceResult.activation;
				if (activation.usesStack()) {
					game.setPriorityPassed(false);
				}
				final MagicSource source=playChoiceResult.source;
				activation.changeActivationPriority(game,source);
				for (final MagicEvent costEvent : activation.getCostEvent(source)) {
					game.addEvent(costEvent);
				}
				final MagicEvent activationEvent=activation.getEvent(source);
				game.addEvent(activationEvent);
			}
		}
	};

	public MagicPriorityEvent(final MagicPlayer player) {
		super(null,player,MagicPlayChoice.getInstance(),MagicEvent.NO_DATA,EVENT_ACTION,null);
	}
}
