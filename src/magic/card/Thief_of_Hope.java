package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

// this card ignores the part that deals with Arcane spells
public class Thief_of_Hope {
    public static final MagicWhenOtherSpellIsCastTrigger T2 = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
            final MagicPlayer player = permanent.getController();
            return (data.getCard().getOwner() == player &&
                    data.getCardDefinition().hasSubType(MagicSubType.Spirit)) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        player.getOpponent() + " loses 1 life and " +
                        player + " gains 1 life."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player.getOpponent(),-1));
            game.doAction(new MagicChangeLifeAction(player,1));
        }        
    };
}
