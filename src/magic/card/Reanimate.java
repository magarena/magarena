package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Reanimate {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                    new MagicGraveyardTargetPicker(false), // no mana cost but payed with life
                    this,
                    "Put target creature card$ from a graveyard onto the battlefield under your control. " +
                    "PN loses life equal to its converted mana cost.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    final MagicPlayer player=event.getPlayer();
                    game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
                    game.doAction(new MagicChangeLifeAction(player,-targetCard.getCardDefinition().getConvertedCost()));
                }
            });
        }
    };
}
