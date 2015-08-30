package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.DrawAction;
import magic.model.event.MagicExcludeEvent;
import magic.model.trigger.MagicTriggerType;

public class MagicDrawPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicDrawPhase();

    public MagicDrawPhase() {
        super(MagicPhaseType.Draw);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        //skip draw phase for first turn
        if (game.getTurn() == 1) {
            game.setStep(MagicStep.NextPhase);
            return;
        }

        final MagicPlayer player=game.getTurnPlayer();
        game.doAction(new DrawAction(player));

        game.executeTrigger(MagicTriggerType.AtDraw,game.getTurnPlayer());

        game.setStep(game.canSkip() ?
            MagicStep.NextPhase :
            MagicStep.ActivePlayer
        );

        // Determines what the purpose is for permanents that can attack,
        // block or produce mana. Do this after draw, could be a land card.
        if (player.isArtificial()) {
            game.addEvent(new MagicExcludeEvent(player));
        }
    }
}
