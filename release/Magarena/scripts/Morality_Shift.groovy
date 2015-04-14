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
                game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Graveyard));
            }
            for (final MagicCard card : graveyard) {
                game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
            }
        }
    }
]
//Should be able to choose the order of cards going into the graveyard.
