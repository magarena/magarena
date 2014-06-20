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
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                if (hand.size() > 0) {
                    for (final MagicCard card : hand) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.OwnersLibrary));
                    }
                    game.doAction(new MagicDrawAction(player,hand.size()));
                }
            }
        }
    }
]
