[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exchange your graveyard and library. Then shuffle your library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList graveyard = new MagicCardList(event.getPlayer().getGraveyard());
            final MagicCardList library = new MagicCardList(event.getPlayer().getLibrary());
            for (final MagicCard card : library) {
                game.doAction(new ShiftCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Graveyard));
            }
            for (final MagicCard card : graveyard) {
                game.doAction(new ShiftCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
            }
        }
    }
]
//Should be able to choose the order of cards going into the graveyard.
