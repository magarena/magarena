[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player discards all the cards in his or her hand, then draws that many cards minus one."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : hand) {
                    game.doAction(new DiscardCardAction(player,card));
                }
                game.doAction(new MagicDrawAction(player, hand.size() - 1));
            }
        }
    }
]
