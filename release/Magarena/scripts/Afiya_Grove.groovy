[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent source, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "PN moves a +1/+1 counter from SN onto target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (event.getPermanent().hasCounters(MagicCounterType.PlusOne)) {
                    game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,-1));
                    game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,1));
                }
            });
        }
    }
]
