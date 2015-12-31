package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;

public class MagicTrampleTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicTrampleTargetPicker INSTANCE=new MagicTrampleTargetPicker();

    private MagicTrampleTargetPicker() {}

    public static MagicTrampleTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final int power = permanent.getPower();
        if (!permanent.hasAbility(MagicAbility.Trample)&&
            !permanent.hasAbility(MagicAbility.Defender)&&
            !permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            if (permanent.isBlocked()) {
                int blockersToughness = 0;
                final MagicPermanentList blockers = permanent.getBlockingCreatures();
                for (final MagicPermanent blocker : blockers) {
                    blockersToughness += blocker.getToughness();
                }
                if (blockersToughness < power ||
                   (permanent.hasAbility(MagicAbility.Deathtouch) && power > blockers.size())) {
                    return 10+power;
                }
            }
            return 1+power;
        }
        return 0;
    }
}
