[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player sacrifices X lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            for (final MagicPlayer player : game.getAPNAP()) {
                for (final int count = 1; count <= amount; count++) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_LAND));
                }
            }
        }
    }
]
