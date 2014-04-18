package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicTriggerType;

public class MagicDeclareBlockerAction extends MagicAction {

    private final MagicPermanent attacker;
    private final MagicPermanent blocker;

    MagicDeclareBlockerAction(final MagicPermanent attacker,final MagicPermanent blocker) {
        this.attacker=attacker;
        this.blocker=blocker;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new MagicSetBlockerAction(attacker, blocker));
        game.executeTrigger(MagicTriggerType.WhenBlocks,blocker);
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
