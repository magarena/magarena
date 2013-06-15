[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            return (permanent == left) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 2 life for each age counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge) * 2;
            game.doAction(new MagicChangeLifeAction(
                event.getPlayer(),
                amount
            ));
        }
    }
]
