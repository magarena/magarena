package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDiscardCardAction;
import magic.model.choice.MagicCardChoice;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.choice.MagicRandomCardChoice;

public class MagicDiscardEvent extends MagicEvent {

    public MagicDiscardEvent(final MagicSource source,final MagicPlayer player,final int amount,final boolean random) {
        super(
            source,
            player,
            random ? new MagicRandomCardChoice(amount) : new MagicCardChoice(amount),
            EVENT_ACTION,
            "PN " + genDescription(player,amount)
        );
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
            final MagicPlayer player=event.getPlayer();
            final MagicCardChoiceResult cards=(MagicCardChoiceResult)choices[0];
            for (final MagicCard card : cards) {
                game.doAction(new MagicDiscardCardAction(player,card));
            }
        }
    };
    
    private static final String genDescription(final MagicPlayer player,final int amount) {
        final int actualAmount = Math.min(amount,player.getHandSize());
        String description = "";
        switch (actualAmount) {
            case 0:
                description = " has no cards to discard.";
                break;
            case 1:
                description = " discards a card$.";
                break;
            default :
                description = " discards " + amount + " cards$.";
        }
        return description;
    }
}
