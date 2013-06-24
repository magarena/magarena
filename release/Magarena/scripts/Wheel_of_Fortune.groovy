[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player discards his or her hand and draws seven cards."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : hand) {
                    game.doAction(new MagicDiscardCardAction(player,card));
                }
                game.doAction(new MagicDrawAction(player, 7));
            }
        }
    }
]
