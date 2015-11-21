[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(
                event.getSource(),
                event.getPlayer()
            ));
        }
    }
]
