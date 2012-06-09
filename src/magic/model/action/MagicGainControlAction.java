package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

public class MagicGainControlAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicPermanent permanent;
    private MagicPlayer oldController;
    
    public MagicGainControlAction(final MagicPlayer player,final MagicPermanent permanent) {
        this.player = player;
        this.permanent = permanent;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        oldController=permanent.getController();
        if (player!=oldController) {
            int score = permanent.getScore();
            
            // Execute trigger here so that full permanent state is preserved.
            game.executeTrigger(MagicTriggerType.WhenLoseControl, permanent);
            
            oldController.removePermanent(permanent);
            permanent.setController(player);
            player.addPermanent(permanent);
            permanent.setState(MagicPermanentState.Summoned);
            if (permanent.getPairedCreature().isValid()) {;
                game.doAction(new MagicSoulbondAction(permanent,permanent.getPairedCreature(),false));
            }
            score += permanent.getScore();
            setScore(player, score);
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (player!=oldController) {
            player.removePermanent(permanent);
            permanent.setController(oldController);
            oldController.addPermanent(permanent);
            permanent.clearState(MagicPermanentState.Summoned);
        }
    }
}
