[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent(),
                    this,
                    "PN puts a -1/-1 counter on " +
                    permanent.getEnchantedPermanent() + "."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getRefPermanent(),MagicCounterType.MinusOne,1));
        }
    }
]
