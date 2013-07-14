[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player sacrifices a multicolored permanent."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.SACRIFICE_MULTICOLORED_PERMANENT
                ));
            }
        }
    }
]
