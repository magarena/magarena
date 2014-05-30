[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicCardList bodies = new MagicCardList();
            for (final MagicCard card : cardOnStack.getController().getLibrary()) {
                if (card.hasType(MagicType.Creature)) {
                    bodies.add(card);
                }
            }
            return new MagicEvent(
                cardOnStack,
                new MagicFromCardListChoice(bodies, 3, true),
                this,
                "PN searches his or her library for up to three creature cards and puts them into his or her graveyard. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                MagicCard card ->
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Graveyard));
            });
            game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
        }
    }
]
