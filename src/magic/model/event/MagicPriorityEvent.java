package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicPlayChoice;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.phase.MagicStep;

public class MagicPriorityEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {

        final MagicPlayChoiceResult playChoiceResult = event.getPlayChoice();
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
            final MagicSourceActivation<? extends MagicSource> sourceActivation = playChoiceResult.sourceActivation;
            if (sourceActivation.usesStack()) {
                game.setPriorityPassed(false);
            }

            // set activation/priority of controller
            sourceActivation.changeActivationPriority();

            // reset payed costs
            game.resetPayedCost();

            // pay costs without choices first, eg {T}
            for (final MagicEvent costEvent : sourceActivation.getCostEvent()) {
                if (costEvent.hasChoice() == false) {
                    game.executeEvent(costEvent, MagicEvent.NO_CHOICE_RESULTS);
                }
            }

            // then pay costs with choices. eg mana cost
            for (final MagicEvent costEvent : sourceActivation.getCostEvent()) {
                if (costEvent.hasChoice() == true) {
                    game.addCostEvent(costEvent);
                }
            }

            game.addEvent(sourceActivation.getEvent());
        }
    };

    public MagicPriorityEvent(final MagicPlayer player) {
        super(
            MagicSource.NONE,
            player,
            MagicPlayChoice.getInstance(),
            EVENT_ACTION,
            ""
        );
    }
}
