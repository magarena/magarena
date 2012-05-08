package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class MagicSoulbondAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicPermanent pairedCreature;
	private final boolean set;
	
	public MagicSoulbondAction(final MagicPermanent permanent,final MagicPermanent pairedCreature,final boolean set) {
		this.permanent = permanent;
		this.pairedCreature = pairedCreature;
		this.set = set;
	}

	@Override
	public void doAction(final MagicGame game) {
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
		if (set) {
			permanent.setPairedCreature(MagicPermanent.NONE);
			pairedCreature.setPairedCreature(MagicPermanent.NONE);
		} else {
			permanent.setPairedCreature(pairedCreature);
			pairedCreature.setPairedCreature(permanent);
		}
	}
}
