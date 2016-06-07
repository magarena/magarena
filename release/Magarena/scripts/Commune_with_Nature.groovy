def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard chosen ->
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

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top five cards of his or her library. " +
                "PN may reveal a creature card from among the and put it into his or her hand." +
                "Put the rest on the bottom of his or her library in any order."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(5);
            final List<MagicCard> choiceList = player.filterCards(topCards, MagicTargetFilterFactory.CREATURE_CARD_FROM_HAND);
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicFromCardListChoice(choiceList, topCards, 1, true),
                MagicGraveyardTargetPicker.ReturnToHand,
                topCards,
                TakeCard,
                ""
            ));
        }
    }
]
