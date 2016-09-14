[
    new MagicPermanentActivation(
        [MagicConditionFactory.CounterAtLeast(MagicCounterType.PlusOne, 1)],
        new MagicActivationHints(MagicTiming.Main),
        "Move"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{1}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
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
                    game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,-1));
                    game.doAction(new ChangeCountersAction(it,MagicCounterType.PlusOne,1));
                }
            });
        }
    }
]
