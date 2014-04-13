
package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.action.MagicScryComplAction;
import magic.model.action.MagicCardAction;

public class MagicScryXEvent extends MagicEvent {
    
    

    public MagicScryXEvent(final MagicSource source, final MagicPlayer player, final int X) {
          super(source, player, new MagicFromCardListChoice(getScryList(player, X), getActualX(player, X), true),new MagicInteger(getActualX(player, X)),BottomAction,"");
    }
    
    private static int getActualX(final MagicPlayer player, final int X) {
        final int size = player.getLibrary().size();
        return (size > X) ? X : size;
        
    }
    
    private static MagicCardList getScryList(final MagicPlayer player, final int X) {
        final MagicCardList scryList = new MagicCardList();
        final int size = player.getLibrary().size();
        for(int i = 1; i <= getActualX(player, X); i++) {
                scryList.add(player.getLibrary().get(size-i));
        }
        return scryList;
    }
        
    
    private static final MagicEventAction TopAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicScryComplAction(event.getPlayer(), card, false));
                    game.logAppendMessage(event.getPlayer(), event.getPlayer() + " puts back a card to the top of his or her library.");
                }
            });
        }
    };
    
    
    private static final MagicEventAction BottomAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList processedCards = new MagicCardList();
            event.processChosenCards(game, new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    processedCards.add(card);
                    game.doAction(new MagicScryComplAction(event.getPlayer(), card, true));
                    game.logAppendMessage(event.getPlayer(), event.getPlayer() + " moves a card from top of his or her library to the bottom.");
                }
            });
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicFromCardListChoice(getScryList(event.getPlayer(), event.getRefInt()-processedCards.size()), getActualX(event.getPlayer(), event.getRefInt()-processedCards.size())),
                getActualX(event.getPlayer(), event.getRefInt()-processedCards.size()),
                TopAction,
                ""
            ));
        }
    };
}