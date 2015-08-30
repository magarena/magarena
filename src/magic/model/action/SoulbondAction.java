package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class SoulbondAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicPermanent pairedCreature;
    private final boolean set;
    private boolean valid;

    public SoulbondAction(final MagicPermanent permanent,final MagicPermanent pairedCreature,final boolean set) {
        this.permanent = permanent;
        this.pairedCreature = pairedCreature;
        this.set = set;
    }

    @Override
    public void doAction(final MagicGame game) {
        valid = permanent.isValid() && pairedCreature.isValid();
        if (!valid) {
            return;
        }

        if (set) {
            permanent.setPairedCreature(pairedCreature);
            pairedCreature.setPairedCreature(permanent);
        } else {
            permanent.setPairedCreature(MagicPermanent.NONE);
            pairedCreature.setPairedCreature(MagicPermanent.NONE);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (!valid) {
            return;
        }
        
        if (set) {
            permanent.setPairedCreature(MagicPermanent.NONE);
            pairedCreature.setPairedCreature(MagicPermanent.NONE);
        } else {
            permanent.setPairedCreature(pairedCreature);
            pairedCreature.setPairedCreature(permanent);
        }
    }
}
