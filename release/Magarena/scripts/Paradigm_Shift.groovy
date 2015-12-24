[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN exiles all cards from his or her library, then shuffles his or her graveyard into his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            final MagicCardList library = new MagicCardList(player.getLibrary());
            for (final MagicCard cardLibrary : library) {
                game.doAction(new ShiftCardAction(
                    cardLibrary,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.Exile
                ));
            }
            game.doAction(new ShuffleCardsIntoLibraryAction(graveyard, MagicLocationType.Graveyard))
        }
    }
]
