package magic.model.phase;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.ChangeStateAction;
import magic.model.action.UntapAction;
import magic.model.trigger.MagicTriggerType;

public class MagicUntapPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicUntapPhase();

    private MagicUntapPhase() {
        super(MagicPhaseType.Untap);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    private static void untap(final MagicGame game) {
        final MagicPlayer player=game.getTurnPlayer();

        for (final MagicPermanent permanent : player.getPermanents()) {
            if (permanent.hasState(MagicPermanentState.Summoned)) {
                game.doAction(ChangeStateAction.Clear(permanent,MagicPermanentState.Summoned));
                game.doAction(ChangeStateAction.Set(permanent,MagicPermanentState.MustPayEchoCost));
            }
            if (permanent.hasState(MagicPermanentState.DoesNotUntapDuringNext)) {
                game.doAction(ChangeStateAction.Clear(permanent,MagicPermanentState.DoesNotUntapDuringNext));
            } else if (permanent.isTapped() && !permanent.hasAbility(MagicAbility.DoesNotUntap)) {
                game.doAction(new UntapAction(permanent));
            }
        }
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        game.executeTrigger(MagicTriggerType.AtUntap, game.getTurnPlayer());
        untap(game);
        game.setStep(MagicStep.NextPhase);
    }
}
