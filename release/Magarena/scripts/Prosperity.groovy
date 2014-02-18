[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player draws "+amount+" cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            for (final MagicPlayer player : game.getPlayers()) {
                game.doAction(new MagicDrawAction(player,amount));
            }
        }
    }
]
