def TakeTwo = {
    final MagicGame game, final MagicEvent event ->
        event.processChosenCards(game, {
            final MagicCard chosen ->
                final MagicPlayer player = event.getPlayer();
                final MagicCardList top = new MagicCardList(player.getLibrary().getCardsFromTop(7));
                for (final MagicCard card : top) {
                    if (card == chosen) {
                        game.doAction(new ShiftCardAction(
                            card,
                            MagicLocationType.OwnersLibrary,
                            MagicLocationType.OwnersHand
                        ));
                        game.logAppendMessage(player, "A card is put into ${player.getName()}'s hand.");
                    } else {
                        game.doAction(new ShiftCardAction(
                            card,
                            MagicLocationType.OwnersLibrary,
                            MagicLocationType.Graveyard
                        ));
                    }
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
                "PN looks at the top seven cards of his or her library. " +
                "PN puts two of them into his or her hand and the rest into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList topCards = event.getPlayer().getLibrary().getCardsFromTop(7);
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(topCards, 2),
                MagicGraveyardTargetPicker.ReturnToHand,
                TakeTwo,
                ""
            ));
        }
    }
]
//Only processes the first card chosen
