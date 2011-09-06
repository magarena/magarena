package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicPlayTokenAction extends MagicPutIntoPlayAction {

	private final MagicCard card;

	public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition) {
		card=MagicCard.createTokenCard(cardDefinition,player);
	}

	@Override
	protected MagicPermanent createPermanent(final MagicGame game) {
		return game.createPermanent(card,card.getController());
	}
}
