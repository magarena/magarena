package magic.model.phase;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
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

        final MagicPlayer player = game.getTurnPlayer();

        if (this == FIRST_INSTANCE) {
            player.getPermanents().stream().filter(MagicPermanent::isSaga).forEach(
                permanent -> game.doAction(new ChangeCountersAction(player, permanent, MagicCounterType.Lore, 1))
            );

            game.executeTrigger(MagicTriggerType.AtBeginOfFirstMainPhase,game.getTurnPlayer());
        }

        game.setStep(MagicStep.ActivePlayer);
    }

    @Override
    protected void executeEndOfPhase(final MagicGame game) {
        game.decreaseMainPhaseCount();
    }
}
