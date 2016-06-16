package magic.model.phase;

import magic.model.MagicGame;
import magic.model.event.MagicDeclareBlockersEvent;
import magic.model.trigger.MagicTriggerType;

public class MagicDeclareBlockersPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicDeclareBlockersPhase();

    private MagicDeclareBlockersPhase() {
        super(MagicPhaseType.DeclareBlockers);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        game.addEvent(new MagicDeclareBlockersEvent(game.getDefendingPlayer()));
        game.setStep(MagicStep.ActivePlayer);
        //Activate attacking and unblocked triggers after all actions during DeclareBlockers and Fast Effects
        game.getAttackingPlayer().getPermanents().stream().filter
            (permanent -> permanent.isAttacking() && !permanent.isBlocked()).forEach
            (permanent -> game.executeTrigger(MagicTriggerType.WhenAttacksUnblocked, permanent));
    }
}
