[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPlayer opponent = permanent.getOpponent();
            return new MagicEvent(
                permanent,
                opponent,
                this,
                "PN discards a card at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(MagicDiscardEvent.Random(
                event.getSource(),
                event.getPlayer(),
            ));
        }
    }
]
