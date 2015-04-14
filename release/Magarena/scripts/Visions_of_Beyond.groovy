[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Draw a card. If a graveyard has twenty or more cards in it, " +
                "draw three cards instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = (
                game.getPlayer(0).getGraveyard().size() >= 20 ||
                game.getPlayer(1).getGraveyard().size() >= 20
            ) ? 3 : 1;
            game.doAction(new DrawAction(event.getPlayer(),amount));
        }
    }
]
