package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicPlayChoice;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.phase.MagicStep;

public class MagicPriorityEvent extends MagicEvent {
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            
            final MagicPlayChoiceResult playChoiceResult=(MagicPlayChoiceResult)choiceResults[0];
            if (playChoiceResult==MagicPlayChoiceResult.PASS ||
                playChoiceResult==MagicPlayChoiceResult.SKIP) {
                game.incrementPriorityPassedCount();
                // when passing, the last played activation can no longer be played.
                if (playChoiceResult==MagicPlayChoiceResult.PASS) {
                    game.getPriorityPlayer().getActivationPriority().incActivationId();
                }
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

                final MagicSource source = playChoiceResult.source;
                final MagicPlayer player = source.getController();
                
                // set activation/priority of controller
                activation.changeActivationPriority(game,player);
                // reset activation/priority of opponent
                game.getOpponent(player).getActivationPriority().clear();

                for (final MagicEvent costEvent : activation.getCostEvent(source)) {
                    game.addEvent(costEvent);
                }
                final MagicEvent activationEvent=activation.getEvent(source);
                game.addEvent(activationEvent);
            }
        }
    };

    public MagicPriorityEvent(final MagicPlayer player) {
        super(MagicEvent.NO_SOURCE,player,MagicPlayChoice.getInstance(),MagicEvent.NO_DATA,EVENT_ACTION,"");
    }
}
