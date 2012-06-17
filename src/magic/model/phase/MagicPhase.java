package magic.model.phase;

import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.model.action.MagicStackResolveAction;
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
                break;
            case ActivePlayer:
                game.checkState();
                game.addEvent(new MagicPriorityEvent(game.getTurnPlayer()));
                break;
            case OtherPlayer:
                game.checkState();
                game.addEvent(new MagicPriorityEvent(game.getOpponent(game.getTurnPlayer())));
                break;
            case Resolve:
                // Stack can be empty at this point, for instance by a counter unless event.
                if (!game.getStack().isEmpty()) {
                    game.doAction(new MagicStackResolveAction());
                    SoundEffects.playClip(game,SoundEffects.RESOLVE_SOUND);
                }
                if (game.isArtificial()) {
                    // Resolve stack in one go.
                    if (game.getStack().isEmpty()) {
                        // Check for maximum number of passes in a phase.
                        if (game.getPriorityPassedCount()<MAX_PRIORITY_PASSED_COUNT) {
                            game.setStep(MagicStep.ActivePlayer);                                                    
                        } else {
                            game.setStep(MagicStep.NextPhase);
                        }
                    }
                } else {
                    game.setStep(MagicStep.ActivePlayer);                        
                }
                break;
            case NextPhase:
                executeEndOfPhase(game);
                game.changePhase(game.getGameplay().getNextPhase(game));
                break;
        }
    }
}
