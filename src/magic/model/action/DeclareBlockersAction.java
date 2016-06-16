package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicCombatCreature;
import magic.model.choice.MagicDeclareBlockersResult;

public class DeclareBlockersAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicDeclareBlockersResult result;

    public DeclareBlockersAction(final MagicPlayer player, final MagicDeclareBlockersResult result) {
        this.player = player;
        this.result = result;
    }

    @Override
    public void doAction(final MagicGame game) {
        for (final MagicCombatCreature[] creatures : result) {
            if (creatures.length>1) {
                final MagicPermanent attacker=creatures[0].permanent;
                for (int index=1;index<creatures.length;index++) {
                    game.doAction(new DeclareBlockerAction(attacker,creatures[index].permanent));
                }
            }
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
