[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ shuffles his or her graveyard into his or her library. "+
                "PN draw a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList graveyard = new MagicCardList(it.getGraveyard());
                game.doAction(new ShuffleCardsIntoLibraryAction(graveyard, MagicLocationType.Graveyard))
            });
                game.doAction(new DrawAction(event.getPlayer()));
        }
    }
]
