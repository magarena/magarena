[
    new SelfDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.getCounters(MagicCounterType.Time) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts two tokens that are copies of SN onto the battlefield."
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
