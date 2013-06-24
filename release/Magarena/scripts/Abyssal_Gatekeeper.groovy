[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "Each player sacrifices a creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.SACRIFICE_CREATURE
                ));
            }
        }
    }
]
