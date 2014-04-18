package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicShadowTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicShadowTargetPicker INSTANCE = new MagicShadowTargetPicker();

    private MagicShadowTargetPicker() {
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        // no score for ability overlap, not being able to deal combat damage,
        // being blocked or blocking
        if (permanent.hasAbility(MagicAbility.Shadow) ||
            permanent.hasAbility(MagicAbility.CannotAttackOrBlock) ||
            permanent.isBlocked() ||
            permanent.isBlocking()) {
            return 0;
        }

        // attacking
        if (game.getTurnPlayer() == player) {
            // no score for not being able to attack or being unblockable
            if (!permanent.canAttack() ||
                !permanent.canBeBlocked(player.getOpponent())) {
                return 0;
            }
        }
        // blocking
        else {
            // no score for not being able to block
            // or opponent has no shadow creatures
            if (!permanent.canBlock() ||
                !player.getOpponent().controlsPermanent(MagicAbility.Shadow)) {
                return 0;
            }
        }

        return 10 + permanent.getPower();
    }

    public static MagicShadowTargetPicker create() {
        return INSTANCE;
    }
}
