package magic.model.phase;

import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.model.action.StackResolveAction;
import magic.model.event.MagicPriorityEvent;

public abstract class MagicPhase {

    private static final int MAX_PRIORITY_PASSED_COUNT=6;

    private final MagicPhaseType type;

    protected abstract void executeBeginStep(final MagicGame game);

    MagicPhase(final MagicPhaseType type) {
        this.type=type;
    }

    public final MagicPhaseType getType() {
        return type;
    }

    void executeEndOfPhase(final MagicGame game) {}

    public void executePhase(final MagicGame game) {
        switch (game.getStep()) {
            case Begin:
                executeBeginStep(game);
                game.update();
                break;
            case ActivePlayer:
                game.checkState();
                game.addEvent(new MagicPriorityEvent(game.getTurnPlayer()));
                break;
            case OtherPlayer:
                game.checkState();
                game.addEvent(new MagicPriorityEvent(game.getTurnPlayer().getOpponent()));
                break;
            case Resolve:
                // Stack can be empty at this point, for instance by a counter unless event.
                if (!game.getStack().isEmpty()) {
                    game.doAction(new StackResolveAction());
                    SoundEffects.playGameSound(game,SoundEffects.RESOLVE_SOUND);
                }

                // Determine next step
                if (game.isReal()) {
                    game.setStep(MagicStep.ActivePlayer);
                } else if (game.getStack().size() > 0) {
                    // Continue to resolve if stack if not empty
                    game.setStep(MagicStep.Resolve);
                } else if (game.getPriorityPassedCount()<MAX_PRIORITY_PASSED_COUNT) {
                    // Check for maximum number of passes in a phase.
                    game.setStep(MagicStep.ActivePlayer);
                } else {
                    game.setStep(MagicStep.NextPhase);
                }

                // If next step is not ActivePlayer, check state first
                if (game.getStep() != MagicStep.ActivePlayer) {
                    game.checkState();
                }

                break;
            case NextPhase:
                executeEndOfPhase(game);
                game.update();
                game.nextPhase();
                break;
        }
    }
}
