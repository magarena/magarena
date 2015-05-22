[
    new MagicPermanentActivation(
        [MagicCondition.CAN_UNTAP_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicUntapEvent(source), new MagicPayManaCostEvent(source, "{2}{G/U}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PERMANENT, //Plus artificial TARGET_PERMANENT_WITH_COUNTERS?
                this,
                "For each counter on target permanent\$, PN puts another of those counters on that permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicCounterType counterType : MagicCounterType.values()) {
                    if (it.hasCounters(counterType)) {
                        game.doAction(new ChangeCountersAction(it, counterType, it.getCounters(counterType)));
                    }
                }
            });
        }
    }
]
//AI relucatant/hardly ever uses untap ability
