package magic.model.phase;

import magic.model.MagicGame;
import magic.model.trigger.MagicTriggerType;

public class MagicBeginOfCombatPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicBeginOfCombatPhase();

    private MagicBeginOfCombatPhase() {
        super(MagicPhaseType.BeginOfCombat);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        // Begin of combat triggers
        game.executeTrigger(MagicTriggerType.AtBeginOfCombat,game.getTurnPlayer());

        game.setStep(MagicStep.ActivePlayer);
    }
}
