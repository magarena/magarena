[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.getCounters(MagicCounterType.Death) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "Return SN to the battlefield under PN's control and put a death counter on it."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReanimateAction(
                event.getPermanent().getCard(),
                event.getPlayer(),
                [MagicPlayMod.DEATH_COUNTER]
            ));
        }
    },
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.hasCounters(MagicCounterType.Death) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Exile SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(
                event.getPermanent().getCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.Exile
            ));
        }
    }
]
