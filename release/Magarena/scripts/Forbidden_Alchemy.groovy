def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard chosen ->
        final MagicCardList library = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(4));
        for (final MagicCard card : library) {
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.OwnersLibrary,
                card == chosen ?
                    MagicLocationType.OwnersHand:
                    MagicLocationType.Graveyard
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
                "PN looks at the top four cards of his or her library. PN puts one of them into "+
                "his or her hand and the rest into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> topCards = player.getLibrary().getCardsFromTop(4);
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicFromCardListChoice(topCards,1),
                MagicGraveyardTargetPicker.ReturnToHand,
                TakeCard,
                "PN puts a card into his or her hand."
            ));
        }
    }
]
