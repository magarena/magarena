package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicLocationType;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.action.ShiftCardAction;
import magic.model.action.RevealAction;
import magic.model.choice.MagicFromCardListChoice;

import java.util.List;

public class MagicTutorTopEvent {

    private static final MagicEventAction RevealHandBottom = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, (final MagicCard chosen) -> {
                for (final MagicCard card : event.getRefCardList()) {
                    if (card == chosen) {
                        game.doAction(new RevealAction(card));
                    }
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        card == chosen ?
                            MagicLocationType.OwnersHand :
                            MagicLocationType.BottomOfOwnersLibrary
                    ));
                }
            });
        }
    };
    
    private static final MagicEventAction HandGraveyard = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, (final MagicCard chosen) -> {
                for (final MagicCard card : event.getRefCardList()) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        card == chosen ?
                            MagicLocationType.OwnersHand :
                            MagicLocationType.Graveyard
                    ));
                }
            });
        }
    };
    
    public static MagicEvent create(final MagicEvent event, final int n, final MagicTargetFilter<MagicCard> filter) {
        final MagicPlayer player = event.getPlayer();
        final MagicCardList topCards = player.getLibrary().getCardsFromTop(n);
        final List<MagicCard> choiceList = player.filterCards(topCards, filter);
        return new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(choiceList, topCards, 1, true),
            MagicGraveyardTargetPicker.ReturnToHand,
            topCards,
            RevealHandBottom,
            ""
        );
    }
    
    public static MagicEvent create(final MagicEvent event, final int n, final int h) {
        final MagicPlayer player = event.getPlayer();
        final MagicCardList topCards = player.getLibrary().getCardsFromTop(n);
        return new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(topCards, h),
            MagicGraveyardTargetPicker.ReturnToHand,
            topCards,
            HandGraveyard,
            ""
        );
    }
}
