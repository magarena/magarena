[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN discards PN's hand. Draws seven cards then discards three cards at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList hand = new MagicCardList(player.getHand());
            for (final MagicCard card : hand) {
                game.doAction(new DiscardCardAction(player,card));
            }
            game.doAction(new DrawAction(player, 7));
            game.addEvent(MagicDiscardEvent.Random(event.getSource(), player, 3));
        }
    }
]
