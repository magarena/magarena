[
    new WhenSelfDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.getCounters(MagicCounterType.Time) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 6/1 green Insect creature token with shroud onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("6/1 green Insect creature token with shroud")
            ));
        }
    }
]
