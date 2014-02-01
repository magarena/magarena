[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
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
