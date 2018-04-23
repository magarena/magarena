package magic.model.phase;

import magic.model.MagicGame;
import magic.model.trigger.MagicTriggerType;

public class MagicMainPhase extends MagicPhase {

    private static final MagicPhase FIRST_INSTANCE=new MagicMainPhase(MagicPhaseType.FirstMain);
    private static final MagicPhase SECOND_INSTANCE=new MagicMainPhase(MagicPhaseType.SecondMain);

    private MagicMainPhase(final MagicPhaseType phaseType) {
        super(phaseType);
    }

    public static MagicPhase getFirstInstance() {
        return FIRST_INSTANCE;
    }

    public static MagicPhase getSecondInstance() {
        return SECOND_INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        if (this == FIRST_INSTANCE) {
            game.executeTrigger(MagicTriggerType.AtBeginOfFirstMainPhase,game.getTurnPlayer());
        }

        game.setStep(MagicStep.ActivePlayer);
    }

    @Override
    protected void executeEndOfPhase(final MagicGame game) {
        game.decreaseMainPhaseCount();
    }
}
