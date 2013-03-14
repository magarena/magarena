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
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.addEvent(new MagicDiscardEvent(
                event.getSource(),
                event.getPlayer(),
                1,
                true
            ));
        }
    }
]
