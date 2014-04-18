package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicNoCombatTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private final boolean noAttacking;
    private final boolean noBlocking;
    private final boolean forever;

    public MagicNoCombatTargetPicker(final boolean noAttacking,final boolean noBlocking,final boolean forever) {
        this.noAttacking=noAttacking;
        this.noBlocking=noBlocking;
        this.forever=forever;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        // For each turn.
        if (forever) {
            final MagicPowerToughness pt=permanent.getPowerToughness();
            int score=pt.power()*2+pt.toughness();
            if (!permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
                if (noAttacking&&!permanent.hasAbility(MagicAbility.Defender)) {
                    score+=100;
                }
                if (noBlocking&&!permanent.hasAbility(MagicAbility.CannotBlock)) {
                    score+=50;
                }
            }
            return score;
        }

        // Until end of turn.
        final boolean turn=game.getTurnPlayer()==player;
        if (turn) {
            if (!noBlocking||!permanent.canBlock()) {
                return 0;
            }
        } else if (!noAttacking||!permanent.canAttack()) {
            return 0;
        }
        final MagicPowerToughness pt=permanent.getPowerToughness();
        return pt.power()*2+pt.toughness()-permanent.getDamage()+1;
    }
}
