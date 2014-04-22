
package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicScryComplAction;
import magic.model.choice.MagicFromCardListChoice;

public class MagicScryXEvent extends MagicEvent {

    public MagicScryXEvent(final MagicSource source, final MagicPlayer player, final int X) {
          super(source, player, X, startScrying(source, player, X), "");
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
        
    private static MagicEventAction startScrying(final MagicSource source, final MagicPlayer player, final int X) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.addFirstEvent(new MagicEvent(
                    source, 
                    player, 
                    new MagicFromCardListChoice(getScryList(player, X), 
                    getActualX(player, X), true, "to be put on the bottom of your library"), 
                    new MagicInteger(getActualX(player, X)), 
                    BottomAction, 
                    ""
                ));
            }
        };
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
            final int actualX =  getActualX(event.getPlayer(), event.getRefInt() - processedCards.size());
            //escape, no sense in calling TopAction if there is no choice to make
            //addFirstEvent, Bottom and TopAction need to be execute in immediate succession, otherwise it breaks     
            if(actualX > 1) {                                     
                game.addFirstEvent(new MagicEvent(                    
                    event.getSource(),
                    event.getPlayer(),
                    new MagicFromCardListChoice(getScryList(event.getPlayer(), actualX), actualX, "to be put on the top of your library"),
                    actualX,
                    TopAction,
                    ""
                ));
            }
        }
    };
}
