[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN discards a card at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(MagicDiscardEvent.Random(
                event.getSource(),
                event.getPlayer()
            ));
        }
    }
]
