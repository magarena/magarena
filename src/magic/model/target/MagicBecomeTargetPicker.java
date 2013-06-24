package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

/** Creatures from both players. */
public class MagicBecomeTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private final int power;
    private final int toughness;
    private final boolean flying;

    public MagicBecomeTargetPicker(final int power,final int toughness,final boolean flying) {
        this.power=power;
        this.toughness=toughness;
        this.flying=flying;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final MagicPowerToughness pt=permanent.getPowerToughness();
        int score=(pt.power()-power)*2+(pt.toughness()-toughness);
        if (flying&&!permanent.hasAbility(MagicAbility.Flying)) {
            score-=5;
        }
        return permanent.getController()==player?-score:score;
    }
}
