package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.trigger.MagicLifeChangeTriggerData;
import magic.model.trigger.MagicTriggerType;

public class ChangeLifeAction extends MagicAction {

    private final MagicPlayer player;
    private final boolean isDamage;
    private int lifeChange;
    private int oldLife;
    private int oldLifeGain;
    private int oldLifeLoss;

    public ChangeLifeAction(final MagicPlayer aPlayer,final int aLifeChange, final boolean aIsDamage) {
        player = aPlayer;
        lifeChange = aLifeChange;
        isDamage = aIsDamage;
    }

    public ChangeLifeAction(final MagicPlayer aPlayer,final int aLifeChange) {
        this(aPlayer, aLifeChange, false);
    }

    public MagicPlayer getPlayer() {
        return player;
    }

    public int getLifeChange() {
        return lifeChange;
    }

    public boolean isDamage() {
        return isDamage;
    }

    public int getOldLife() {
        return oldLife;
    }

    public int getNewLife() {
        return oldLife + lifeChange;
    }

    public void setLifeChange(final int aLifeChange) {
        lifeChange = aLifeChange;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldLife = player.getLife();
        oldLifeGain = player.getLifeGainThisTurn();
        oldLifeLoss = player.getLifeLossThisTurn();

        game.executeTrigger(MagicTriggerType.IfLifeWouldChange, this);

        final int newLife = oldLife + lifeChange;
        player.setLife(newLife);

        setScore(player,ArtificialScoringSystem.getLifeScore(newLife)-ArtificialScoringSystem.getLifeScore(oldLife));
        if (newLife > oldLife) {
            game.executeTrigger(MagicTriggerType.WhenLifeIsGained,new MagicLifeChangeTriggerData(player,newLife-oldLife));
            game.doAction(new ChangePlayerStateAction(player,MagicPlayerState.HasGainedLife));
            player.changeLifeGainThisTurn(newLife-oldLife);
        } else if (newLife < oldLife) {
            game.executeTrigger(MagicTriggerType.WhenLifeIsLost,new MagicLifeChangeTriggerData(player,oldLife-newLife));
            game.doAction(new ChangePlayerStateAction(player,MagicPlayerState.HasLostLife));
            player.changeLifeLossThisTurn(oldLife-newLife);
        }
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.setLife(oldLife);
        player.setLifeGainThisTurn(oldLifeGain);
        player.setLifeLossThisTurn(oldLifeLoss);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+player.getName()+','+lifeChange+')';
    }
}
