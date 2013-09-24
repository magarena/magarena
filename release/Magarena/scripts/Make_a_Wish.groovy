[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return two cards at random from your graveyard to your hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getGraveyard();
            final MagicRandom rng = new MagicRandom(cards.getStateId());
            int actualAmount = Math.min(cards.size(),2);
            for (;actualAmount>0;actualAmount--) {
                final int index = rng.nextInt(cards.size());
                final MagicCard card = cards.get(index);
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
