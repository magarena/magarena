[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "PN puts RN tower counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.Tower, event.getRefInt()));
        }
    }
]
