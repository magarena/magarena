package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.choice.MagicDelayedPayManaCostResult;

public class PayDelayedCostAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicDelayedPayManaCostResult result;
    private MagicBuilderManaCost oldBuilderCost;

    public PayDelayedCostAction(final MagicPlayer player,final MagicDelayedPayManaCostResult result) {
        this.player=player;
        this.result=result;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldBuilderCost=player.getBuilderCost();
        final MagicBuilderManaCost builderCost=new MagicBuilderManaCost(oldBuilderCost);
        result.getCost().addTo(builderCost,result.getX());
        player.setBuilderCost(builderCost);
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.setBuilderCost(oldBuilderCost);
    }
}
