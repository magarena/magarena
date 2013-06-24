package magic.model.phase;

import magic.model.MagicGame;
import magic.model.event.MagicDeclareAttackersEvent;

public class MagicDeclareAttackersPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicDeclareAttackersPhase();

    private MagicDeclareAttackersPhase() {
        super(MagicPhaseType.DeclareAttackers);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        game.addEvent(new MagicDeclareAttackersEvent(game.getTurnPlayer()));
        game.setStep(MagicStep.ActivePlayer);
    }
}
