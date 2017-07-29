package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MurmurHash3;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class ChangeTurnPTAction extends MagicAction {

    private final MagicPermanent permanent;
    private final int power;
    private final int toughness;

    public ChangeTurnPTAction(final MagicPermanent permanent,final int power,final int toughness) {
        this.permanent=permanent;
        this.power=power;
        this.toughness=toughness;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new AddStaticAction(
            permanent,
            new MagicStatic(MagicLayer.ModPT, MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                    pt.add(power, toughness);
                }
                @Override
                public long getStateId() {
                    return MurmurHash3.hash(new long[] {
                        MagicLayer.ModPT.ordinal(),
                        MagicStatic.UntilEOT ? -1L : 1L,
                        power,
                        toughness
                    });
                }
            }
        ));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
