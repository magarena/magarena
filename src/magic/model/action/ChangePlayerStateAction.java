package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.AtUpkeepTrigger;

public class ChangePlayerStateAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicPlayerState state;
    private boolean setPlayer = false;

    public ChangePlayerStateAction(final MagicPlayer aPlayer,final MagicPlayerState aState) {
        player = aPlayer;
        state = aState;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (player.hasState(state)) {
            // do nothing
        } else if (state == MagicPlayerState.Exhausted) {
            // no duration, manually removed during player's next upkeep
            final MagicStatic exhausted = new MagicStatic(MagicLayer.Player) {
                @Override
                public void modPlayer(final MagicPermanent source, final MagicPlayer aPlayer) {
                    if (player.getId() == aPlayer.getId()) {
                        aPlayer.setState(state);
                    }
                }
            };
            game.doAction(new AddStaticAction(exhausted));

            AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    if (player.getId() == upkeepPlayer.getId()) {
                        game.addDelayedAction(new RemoveStaticAction(exhausted));
                        game.addDelayedAction(new RemoveTriggerAction(this));
                    }
                    return MagicEvent.NONE;
                }
            };
            game.doAction(new AddTriggerAction(cleanup));
        } else {
            player.setState(state);
            setPlayer = true;
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (setPlayer) {
            player.clearState(state);
        }
    }
}
