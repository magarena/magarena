package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicDeclareAttackerAction extends MagicAction {

    private final MagicPermanent attacker;

    MagicDeclareAttackerAction(final MagicPermanent attacker) {
        this.attacker=attacker;
    }

    @Override
    public void doAction(final MagicGame game) {
        attacker.setState(MagicPermanentState.Attacking);
        attacker.getController().incCreatuesAttacked();
    }

    @Override
    public void undoAction(final MagicGame game) {
        attacker.clearState(MagicPermanentState.Attacking);
        attacker.getController().decCreaturesAttacked();
    }
}
