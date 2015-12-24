[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player shuffles the cards from his or her hand into his or her library, then draws that many cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                game.doAction(new ShuffleCardsIntoLibraryAction(hand, MagicLocationType.OwnersHand));
                game.doAction(new DrawAction(player,hand.size()));
            }
        }
    }
]
// Can't work out a way of remembering previous amount of shuffled cards to split the discard and draw into two loops
