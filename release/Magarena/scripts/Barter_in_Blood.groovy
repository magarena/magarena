[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player sacrifices two creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                for (int i=2;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        SACRIFICE_CREATURE
                    ));
                }
            }
        }
    }
]
