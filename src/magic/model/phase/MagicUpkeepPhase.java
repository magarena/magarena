package magic.model.phase;

import magic.model.MagicGame;
import magic.model.trigger.MagicTriggerType;

public class MagicUpkeepPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicUpkeepPhase();

    private MagicUpkeepPhase() {
        super(MagicPhaseType.Upkeep);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        game.executeTrigger(MagicTriggerType.AtUpkeep,game.getTurnPlayer());
        game.setStep(game.canSkip() ?
            MagicStep.NextPhase:
            MagicStep.ActivePlayer
        );
    }
}
