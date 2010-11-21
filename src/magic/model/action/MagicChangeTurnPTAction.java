package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class MagicChangeTurnPTAction extends MagicAction {

	private final MagicPermanent permanent;
	private final int power;
	private final int toughness;
	
	public MagicChangeTurnPTAction(final MagicPermanent permanent,final int power,final int toughness) {
		
		this.permanent=permanent;
		this.power=power;
		this.toughness=toughness;
	}
	
	@Override
	public void doAction(final MagicGame game) {

		permanent.changeTurnPower(power);
		permanent.changeTurnToughness(toughness);
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

		permanent.changeTurnPower(-power);
		permanent.changeTurnToughness(-toughness);
	}
}