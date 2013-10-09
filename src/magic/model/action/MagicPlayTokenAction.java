package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicObject;

public class MagicPlayTokenAction extends MagicPutIntoPlayAction {

    private final MagicCard card;

    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition) {
        card=MagicCard.createTokenCard(cardDefinition,player);
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj) {
        this(player, obj.getCardDefinition());
    }

    public MagicPlayTokenAction(final MagicCard aCard) {
        card=aCard;
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        return game.createPermanent(card,card.getController());
    }
}
