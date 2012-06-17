package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.mstatic.MagicCDA;

public class Tarmogoyf {
    public static final MagicCDA CDA = new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            MagicCardList cardList = new MagicCardList(player.getGraveyard());
            cardList.addAll(game.getOpponent(player).getGraveyard());
            int types = 0;
            for (final MagicType type : MagicType.ALL_CARD_TYPES) {
                for (final MagicCard card : cardList) {
                    if (card.getCardDefinition().hasType(type)) {
                        types++;
                        break;
                    }
                }
            }        
            pt.set(types, types + 1);
        }
    };
}
