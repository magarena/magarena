[
    new MagicWhenSelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                this,
                "PN discards a card at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(MagicDiscardEvent.Random(
                event.getSource(),
                event.getPlayer()
            ));
        }
    }
]
