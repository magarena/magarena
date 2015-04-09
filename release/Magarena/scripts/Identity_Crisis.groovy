[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Exile all cards from target player\$'s hand and graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : new MagicCardList(it.getHand())) {
                    game.doAction(new MagicRemoveCardAction(card, MagicLocationType.OwnersHand));
                    game.doAction(new MagicMoveCardAction(card, MagicLocationType.OwnersHand, MagicLocationType.Exile));
                }
                for (final MagicCard card : new MagicCardList(it.getGraveyard())) {
                    game.doAction(new MagicRemoveCardAction(card, MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                }
            });
        }
    }
]
