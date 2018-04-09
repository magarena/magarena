def choice = MagicTargetChoice.Positive("another target creature you control with a +1/+1 counter on it")

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Move"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{2}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicPumpTargetPicker.create(),
                this,
                "PN moves a +1/+1 counter from target creature you control\$ onto SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (it.hasCounters(MagicCounterType.PlusOne)) {
                    game.doAction(new ChangeCountersAction(event.getPlayer(), it, MagicCounterType.PlusOne, -1));
                    game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(), MagicCounterType.PlusOne, 1));
                }
            });
        }
    }
]
