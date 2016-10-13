[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.getCounters(MagicCounterType.Time) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN creates two tokens that are copies of SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                event.getPermanent(),
                2
            ));
        }
    }
]
