[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return permanent.getCounters(MagicCounterType.Charge) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 6/1 green Insect creature token " +
                    "with shroud onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Insect2")
            ));
        }
    }
]
