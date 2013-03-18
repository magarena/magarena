[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final MagicPlayer opponent = player.getOpponent();
            return new MagicEvent(
                permanent,
                opponent,
                this,
                "PN discards a card."
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
                false
            ));
        }        
    }
]
