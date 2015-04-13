[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{W}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getEnchantedPermanent(),
                this,
                "PN puts a +1/+1 counter on RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));
        }
    }
]
