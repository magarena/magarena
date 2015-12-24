[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "PN shuffles all cards from his or her graveyard into his or her library. "+
                "Target player\$ puts that many cards from the top of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                game.doAction(new ShuffleCardsIntoLibraryAction(graveyard, MagicLocationType.Graveyard))
                game.doAction(new MillLibraryAction(it,graveyard.size()));
            });
        }
    }
]
