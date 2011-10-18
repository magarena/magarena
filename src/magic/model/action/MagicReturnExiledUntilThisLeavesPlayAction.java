package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class MagicReturnExiledUntilThisLeavesPlayAction extends MagicAction {

	private final MagicPermanent source;
	private final MagicLocationType location;
	private MagicCardList exiledList;
	
	public MagicReturnExiledUntilThisLeavesPlayAction(final MagicPermanent source,final MagicLocationType location) {
		this.source = source;
		this.location = location;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		final MagicCardList cardList = source.getExiledCards();
		exiledList = new MagicCardList(source.getExiledCards());
			for (final MagicCard card : cardList) {
				if (card.getOwner().getExile().contains(card)) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Exile));
					if (location == MagicLocationType.Play) {
						game.doAction(new MagicPlayCardAction(card,card.getOwner(),MagicPlayCardAction.NONE));
					} else {
						game.doAction(new MagicMoveCardAction(card,MagicLocationType.Exile,location));
					} 
				}
			}
			cardList.clear();
	}

	@Override
	public void undoAction(final MagicGame game) {
		source.getExiledCards().addAll(exiledList);
	}
}
