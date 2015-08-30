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
            final MagicCardList graveyard = new MagicCardList(event.getPlayer().getGraveyard());
            final MagicCardList library = new MagicCardList(event.getPlayer().getLibrary());
            for (final MagicCard cardLibrary : library) {
                game.doAction(new ShiftCardAction(cardLibrary,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
            }
            for (final MagicCard cardGraveyard : graveyard) {
                game.doAction(new ShiftCardAction(cardGraveyard,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
            }
        }
    }
]
